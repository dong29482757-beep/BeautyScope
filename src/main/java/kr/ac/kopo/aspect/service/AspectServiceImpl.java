package kr.ac.kopo.aspect.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ac.kopo.aspect.dao.AspectDAO;
import kr.ac.kopo.aspect.vo.AspectVO;
import kr.ac.kopo.aspect.vo.RecommendCandidateVO;
import kr.ac.kopo.aspect.vo.RecommendVO;

@Service
public class AspectServiceImpl implements AspectService {

	// 베이지안 보정 강도: 클수록 리뷰가 적은 제품의 점수를 카테고리 평균쪽으로 더 세게 당긴다.
	private static final double ASPECT_PRIOR_WEIGHT = 15.0;
	private static final double OVERALL_PRIOR_WEIGHT = 30.0;

	// 최종 점수 = 가중합(선택 속성 만족도, 전체 만족도, 평점). 합은 1.0.
	private static final double WEIGHT_ASPECT = 0.5;
	private static final double WEIGHT_OVERALL = 0.3;
	private static final double WEIGHT_RATING = 0.2;

	@Autowired
	private AspectDAO aspectDAO;

	@Override
	public List<AspectVO> getAspects(String platform, String productId) {
		return aspectDAO.selectByProduct(platform, productId);
	}

	@Override
	public List<String> getAspectNames() {
		return aspectDAO.selectAspectNames();
	}

	@Override
	public List<RecommendVO> getRecommend(String category, String aspect) {
		return aspectDAO.selectRecommend(category, aspect);
	}

	@Override
	public List<RecommendVO> getRecommend(String category, List<String> aspects) {
		List<RecommendCandidateVO> rows = aspectDAO.selectRecommendCandidates(category, aspects);
		if (rows.isEmpty()) {
			return new ArrayList<>();
		}

		// 1) 속성별 카테고리 전체 평균 긍정비율(베이지안 사전 평균)을 먼저 구한다.
		Map<String, long[]> aspectTotals = new LinkedHashMap<>(); // aspect -> [posSum, totalSum]
		for (RecommendCandidateVO row : rows) {
			long[] acc = aspectTotals.computeIfAbsent(row.getAspect(), k -> new long[2]);
			acc[0] += row.getPosCnt();
			acc[1] += row.getTotalCnt();
		}
		Map<String, Double> aspectPriorMean = new LinkedHashMap<>();
		for (Map.Entry<String, long[]> e : aspectTotals.entrySet()) {
			long[] acc = e.getValue();
			aspectPriorMean.put(e.getKey(), acc[1] == 0 ? 0.7 : (double) acc[0] / acc[1]);
        }

		// 2) 제품 단위로 묶는다 (제품당 선택 속성 수만큼 행이 있다).
		Map<String, ProductAgg> byProduct = new LinkedHashMap<>();
		for (RecommendCandidateVO row : rows) {
			String key = row.getPlatform() + "::" + row.getProductId();
			ProductAgg agg = byProduct.computeIfAbsent(key, k -> new ProductAgg(row));
			agg.aspectRows.add(row);
		}

		// 3) 전체(속성 무관) 긍정비율의 카테고리 평균 — overall 베이지안 사전 평균
		long overallPosSum = 0, overallTotalSum = 0;
		for (ProductAgg agg : byProduct.values()) {
			overallPosSum += agg.base.getnPositive();
			overallTotalSum += agg.base.getReviewCount();
		}
		double overallPriorMean = overallTotalSum == 0 ? 0.7 : (double) overallPosSum / overallTotalSum;

		// 4) 제품별 합성 점수 계산
		List<RecommendVO> result = new ArrayList<>();
		for (ProductAgg agg : byProduct.values()) {
			RecommendCandidateVO base = agg.base;

			double aspectScoreSum = 0;
			int aspectScoreCount = 0;
			List<String> matched = new ArrayList<>();
			for (RecommendCandidateVO row : agg.aspectRows) {
				if (row.getTotalCnt() <= 0) {
					continue;
				}
				double prior = aspectPriorMean.getOrDefault(row.getAspect(), 0.7);
				double bayesian = (row.getPosCnt() + ASPECT_PRIOR_WEIGHT * prior)
						/ (row.getTotalCnt() + ASPECT_PRIOR_WEIGHT);
				aspectScoreSum += bayesian;
				aspectScoreCount++;
				matched.add(row.getAspect());
			}
			if (aspectScoreCount == 0) {
				continue; // 선택한 속성에 대한 리뷰가 전혀 없는 제품은 추천 후보에서 제외
			}
			double aspectScore = aspectScoreSum / aspectScoreCount;

			double overallRatio = (base.getnPositive() + OVERALL_PRIOR_WEIGHT * overallPriorMean)
					/ (base.getReviewCount() + OVERALL_PRIOR_WEIGHT);

			double ratingNorm = Math.max(0, Math.min(1, base.getAvgRating() / 5.0));

			double score = WEIGHT_ASPECT * aspectScore * 100
					+ WEIGHT_OVERALL * overallRatio * 100
					+ WEIGHT_RATING * ratingNorm * 100;

			RecommendVO vo = new RecommendVO();
			vo.setPlatform(base.getPlatform());
			vo.setProductId(base.getProductId());
			vo.setProductName(base.getProductName());
			vo.setBrandName(base.getBrandName());
			vo.setCategory(category);
			vo.setAvgRating(base.getAvgRating());
			vo.setReviewCount(base.getReviewCount());
			vo.setPositiveRatio(Math.round(aspectScore * 1000) / 10.0);
			vo.setOverallRatio(Math.round(overallRatio * 1000) / 10.0);
			vo.setScore(Math.round(score * 10) / 10.0);
			vo.setMatchedAspects(String.join(", ", matched));
			result.add(vo);
		}

		result.sort(Comparator.comparingDouble(RecommendVO::getScore).reversed());
		return result.size() > 10 ? result.subList(0, 10) : result;
	}

	private static class ProductAgg {
		final RecommendCandidateVO base;
		final List<RecommendCandidateVO> aspectRows = new ArrayList<>();

		ProductAgg(RecommendCandidateVO base) {
			this.base = base;
		}
	}
}
