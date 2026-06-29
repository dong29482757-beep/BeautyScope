package kr.ac.kopo.loader;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * D:\crolling\merged_reviews_all.csv (538,774건)를 두 번 훑어서
 * t_product(요약 집계) -> t_review(전체 원본) 순으로 Oracle에 적재한다.
 *
 * MyBatis를 거치지 않고 순수 JDBC 배치 insert를 쓴다 (1회성 대량적재라
 * 매퍼를 만드는 것보다 직접 적재가 더 빠르고 단순함).
 *
 * 실행: mvn exec:java
 */
public class DataLoader {

	private static final String CSV_PATH = "D:\\crolling\\merged_reviews_all.csv";
	private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
	private static final String JDBC_USER = "hr";
	private static final String JDBC_PASSWORD = "hr";

	// CSV 컬럼 순서 (merge_datasets.py UNIFIED_COLS와 동일, BOM 문제를 피하려고
	// 헤더를 직접 명시하고 첫 줄은 그냥 건너뛴다)
	private static final String[] HEADERS = {
			"platform", "review_id", "product_id", "product_name", "brand_name",
			"category", "rating", "sentiment", "sentiment_text_raw",
			"review_title", "review_content", "review_date", "nickname"
	};

	private static class ProductAgg {
		String productName;
		String brandName;
		String category;
		int reviewCount = 0;
		double ratingSum = 0;
		int negative = 0;
		int neutral = 0;
		int positive = 0;
	}

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();
		Map<String, ProductAgg> productMap = aggregateProducts();
		System.out.println("1단계(집계) 완료: " + productMap.size() + "개 상품, "
				+ (System.currentTimeMillis() - t0) + "ms");

		try (Connection conn = openConnection()) {
			conn.setAutoCommit(false);
			insertProducts(conn, productMap);
			System.out.println("t_product 적재 완료");

			long t1 = System.currentTimeMillis();
			int reviewCount = insertReviews(conn);
			System.out.println("t_review 적재 완료: " + reviewCount + "건, "
					+ (System.currentTimeMillis() - t1) + "ms");
		}

		System.out.println("전체 완료: " + (System.currentTimeMillis() - t0) + "ms");
	}

	private static Connection openConnection() throws SQLException {
		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}

	private static CSVParser openCsv(Reader reader) throws IOException {
		CSVFormat format = CSVFormat.DEFAULT.builder()
				.setHeader(HEADERS)
				.setSkipHeaderRecord(true)
				.setIgnoreSurroundingSpaces(true)
				.build();
		return CSVParser.parse(reader, format);
	}

	private static Reader newCsvReader() throws IOException {
		return Files.newBufferedReader(Path.of(CSV_PATH), StandardCharsets.UTF_8);
	}

	/** 1차 패스: platform+product_id 기준으로 상품 단위 집계만 메모리에 쌓는다. */
	private static Map<String, ProductAgg> aggregateProducts() throws IOException {
		Map<String, ProductAgg> map = new LinkedHashMap<>();
		try (Reader reader = newCsvReader(); CSVParser parser = openCsv(reader)) {
			for (CSVRecord r : parser) {
				String platform = r.get("platform");
				String productId = r.get("product_id");
				if (platform == null || productId == null || platform.isBlank() || productId.isBlank()) {
					continue;
				}
				String key = platform + "|" + productId;
				ProductAgg agg = map.computeIfAbsent(key, k -> new ProductAgg());
				if (agg.productName == null) {
					String name = emptyToNull(r.get("product_name"));
					agg.productName = (name != null) ? name : "(상품명 미확인)";
					agg.brandName = emptyToNull(r.get("brand_name"));
					agg.category = emptyToNull(r.get("category"));
				}
				agg.reviewCount++;
				String ratingStr = r.get("rating");
				if (ratingStr != null && !ratingStr.isBlank()) {
					agg.ratingSum += Double.parseDouble(ratingStr);
				}
				String sentiment = r.get("sentiment");
				if ("negative".equals(sentiment)) {
					agg.negative++;
				} else if ("neutral".equals(sentiment)) {
					agg.neutral++;
				} else if ("positive".equals(sentiment)) {
					agg.positive++;
				}
			}
		}
		return map;
	}

	private static String emptyToNull(String s) {
		return (s == null || s.isBlank()) ? null : s;
	}

	private static void insertProducts(Connection conn, Map<String, ProductAgg> productMap) throws SQLException {
		String sql = "INSERT INTO t_product "
				+ "(platform, product_id, product_name, brand_name, category, "
				+ " review_count, avg_rating, n_negative, n_neutral, n_positive) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			int batch = 0;
			for (Map.Entry<String, ProductAgg> e : productMap.entrySet()) {
				String[] key = e.getKey().split("\\|", 2);
				ProductAgg agg = e.getValue();
				double avgRating = agg.reviewCount > 0 ? agg.ratingSum / agg.reviewCount : 0;

				ps.setString(1, key[0]);
				ps.setString(2, key[1]);
				ps.setString(3, agg.productName);
				ps.setString(4, agg.brandName);
				ps.setString(5, agg.category);
				ps.setInt(6, agg.reviewCount);
				ps.setBigDecimal(7, BigDecimal.valueOf(avgRating).setScale(2, java.math.RoundingMode.HALF_UP));
				ps.setInt(8, agg.negative);
				ps.setInt(9, agg.neutral);
				ps.setInt(10, agg.positive);
				ps.addBatch();
				batch++;
				if (batch % 500 == 0) {
					ps.executeBatch();
				}
			}
			ps.executeBatch();
			conn.commit();
		}
	}

	/** 2차 패스: 리뷰 원본을 그대로 스트리밍 + 배치 insert (538,774건). */
	private static int insertReviews(Connection conn) throws IOException, SQLException {
		String sql = "INSERT INTO t_review "
				+ "(review_no, platform, product_id, rating, sentiment, review_content, review_date, nickname) "
				+ "VALUES (seq_t_review_no.nextval, ?, ?, ?, ?, ?, ?, ?)";
		int total = 0;
		try (Reader reader = newCsvReader();
				CSVParser parser = openCsv(reader);
				PreparedStatement ps = conn.prepareStatement(sql)) {
			int batch = 0;
			for (CSVRecord r : parser) {
				String platform = r.get("platform");
				String productId = r.get("product_id");
				String ratingStr = r.get("rating");
				if (platform == null || productId == null || platform.isBlank() || productId.isBlank()
						|| ratingStr == null || ratingStr.isBlank()) {
					continue;
				}
				ps.setString(1, platform);
				ps.setString(2, productId);
				ps.setBigDecimal(3, BigDecimal.valueOf(Double.parseDouble(ratingStr)));
				ps.setString(4, emptyToNull(r.get("sentiment")));
				ps.setString(5, r.get("review_content"));
				ps.setString(6, emptyToNull(r.get("review_date")));
				ps.setString(7, emptyToNull(r.get("nickname")));
				ps.addBatch();
				batch++;
				total++;
				if (batch >= 1000) {
					ps.executeBatch();
					conn.commit();
					batch = 0;
					if (total % 50000 == 0) {
						System.out.println("  ... " + total + "건 적재");
					}
				}
			}
			if (batch > 0) {
				ps.executeBatch();
				conn.commit();
			}
		}
		return total;
	}
}
