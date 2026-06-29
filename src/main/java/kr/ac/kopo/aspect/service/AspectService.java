package kr.ac.kopo.aspect.service;

import java.util.List;

import kr.ac.kopo.aspect.vo.AspectVO;
import kr.ac.kopo.aspect.vo.RecommendVO;

public interface AspectService {

	List<AspectVO> getAspects(String platform, String productId);

	List<String> getAspectNames();

	List<RecommendVO> getRecommend(String category, String aspect);

	List<RecommendVO> getRecommend(String category, List<String> aspects);
}
