package kr.ac.kopo.aspect.dao;

import java.util.List;

import kr.ac.kopo.aspect.vo.AspectVO;
import kr.ac.kopo.aspect.vo.RecommendCandidateVO;
import kr.ac.kopo.aspect.vo.RecommendVO;

public interface AspectDAO {

	List<AspectVO> selectByProduct(String platform, String productId);

	List<String> selectAspectNames();

	List<RecommendVO> selectRecommend(String category, String aspect);

	List<RecommendCandidateVO> selectRecommendCandidates(String category, List<String> aspects);
}
