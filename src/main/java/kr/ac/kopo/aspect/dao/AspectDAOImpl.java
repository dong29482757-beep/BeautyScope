package kr.ac.kopo.aspect.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.ac.kopo.aspect.vo.AspectVO;
import kr.ac.kopo.aspect.vo.RecommendCandidateVO;
import kr.ac.kopo.aspect.vo.RecommendVO;

@Repository
public class AspectDAOImpl implements AspectDAO {

	private static final String NS = "aspect.dao.AspectDAO.";

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<AspectVO> selectByProduct(String platform, String productId) {
		Map<String, Object> params = new HashMap<>();
		params.put("platform", platform);
		params.put("productId", productId);
		return sqlSessionTemplate.selectList(NS + "selectByProduct", params);
	}

	@Override
	public List<String> selectAspectNames() {
		return sqlSessionTemplate.selectList(NS + "selectAspectNames");
	}

	@Override
	public List<RecommendVO> selectRecommend(String category, String aspect) {
		Map<String, Object> params = new HashMap<>();
		params.put("category", category);
		params.put("aspect", aspect);
		return sqlSessionTemplate.selectList(NS + "selectRecommend", params);
	}

	@Override
	public List<RecommendCandidateVO> selectRecommendCandidates(String category, List<String> aspects) {
		Map<String, Object> params = new HashMap<>();
		params.put("category", category);
		params.put("aspects", aspects);
		return sqlSessionTemplate.selectList(NS + "selectRecommendCandidates", params);
	}
}
