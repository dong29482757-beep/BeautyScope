package kr.ac.kopo.loader;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * D:\crolling\models\aspect_sentiment.csv (27,430건, 속성기반 감성분석 결과)를
 * t_aspect_sentiment에 적재한다. t_product 전체(2,405개)가 아니라 ABSA 대상
 * 1,882개 제품만 들어있는 부분집합 CSV라서 FK 위반 없이 그대로 적재 가능.
 *
 * 실행: mvn exec:java -Dexec.mainClass=kr.ac.kopo.loader.AspectLoader
 */
public class AspectLoader {

	private static final String CSV_PATH = "D:\\crolling\\models\\aspect_sentiment.csv";
	private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
	private static final String JDBC_USER = "hr";
	private static final String JDBC_PASSWORD = "hr";

	private static final String[] HEADERS = {
			"platform", "product_id", "product_name", "aspect", "sentiment", "count"
	};

	public static void main(String[] args) throws Exception {
		long t0 = System.currentTimeMillis();

		// 소스 CSV에 (platform,product_id,aspect,sentiment) 키 중복이 일부(34건) 있어서
		// PK 충돌이 난다 -> 같은 키는 count를 합산해서 적재한다.
		Map<String, Object[]> merged = new LinkedHashMap<>();
		try (Reader reader = Files.newBufferedReader(Path.of(CSV_PATH), StandardCharsets.UTF_8);
				CSVParser parser = CSVFormat.DEFAULT.builder()
						.setHeader(HEADERS)
						.setSkipHeaderRecord(true)
						.build()
						.parse(reader)) {
			for (CSVRecord rec : parser) {
				String platform = rec.get("platform");
				String productId = rec.get("product_id");
				String aspect = rec.get("aspect");
				String sentiment = rec.get("sentiment");
				int cnt = Integer.parseInt(rec.get("count"));

				String key = platform + "|" + productId + "|" + aspect + "|" + sentiment;
				Object[] row = merged.get(key);
				if (row == null) {
					merged.put(key, new Object[] { platform, productId, aspect, sentiment, cnt });
				} else {
					row[4] = (Integer) row[4] + cnt;
				}
			}
		}

		try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
			conn.setAutoCommit(false);

			String sql = "INSERT INTO t_aspect_sentiment (platform, product_id, aspect, sentiment, cnt) "
					+ "VALUES (?, ?, ?, ?, ?)";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				int batchCount = 0;
				int total = 0;
				for (Object[] row : merged.values()) {
					ps.setString(1, (String) row[0]);
					ps.setString(2, (String) row[1]);
					ps.setString(3, (String) row[2]);
					ps.setString(4, (String) row[3]);
					ps.setInt(5, (Integer) row[4]);
					ps.addBatch();
					batchCount++;
					total++;

					if (batchCount >= 1000) {
						ps.executeBatch();
						conn.commit();
						batchCount = 0;
					}
				}
				if (batchCount > 0) {
					ps.executeBatch();
					conn.commit();
				}
				System.out.println("t_aspect_sentiment 적재 완료: " + total + "건, "
						+ (System.currentTimeMillis() - t0) + "ms");
			}
		}
	}
}
