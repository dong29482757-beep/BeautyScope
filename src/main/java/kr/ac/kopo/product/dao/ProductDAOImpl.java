package kr.ac.kopo.product.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.ac.kopo.product.vo.BrandStatVO;
import kr.ac.kopo.product.vo.ProductCriteria;
import kr.ac.kopo.product.vo.ProductVO;
import kr.ac.kopo.product.vo.RatingDistVO;

@Repository
public class ProductDAOImpl implements ProductDAO {

	private static final String NS = "product.dao.ProductDAO.";

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<ProductVO> selectList(ProductCriteria cri) {
		return sqlSessionTemplate.selectList(NS + "selectList", cri);
	}

	@Override
	public int selectListCount(ProductCriteria cri) {
		return sqlSessionTemplate.selectOne(NS + "selectListCount", cri);
	}

	@Override
	public ProductVO selectOne(String platform, String productId) {
		Map<String, Object> params = new HashMap<>();
		params.put("platform", platform);
		params.put("productId", productId);
		return sqlSessionTemplate.selectOne(NS + "selectOne", params);
	}

	@Override
	public List<String> selectCategories() {
		return sqlSessionTemplate.selectList(NS + "selectCategories");
	}

	@Override
	public List<ProductVO> selectTop5() {
		return sqlSessionTemplate.selectList(NS + "selectTop5");
	}

	@Override
	public List<ProductVO> selectRanking(String category) {
		return sqlSessionTemplate.selectList(NS + "selectRanking", category);
	}

	@Override
	public List<RatingDistVO> selectRatingDist(String platform, String productId) {
		Map<String, Object> params = new HashMap<>();
		params.put("platform", platform);
		params.put("productId", productId);
		return sqlSessionTemplate.selectList(NS + "selectRatingDist", params);
	}

	@Override
	public List<BrandStatVO> selectBrandStats(String category) {
		return sqlSessionTemplate.selectList(NS + "selectBrandStats", category);
	}

	@Override
	public Map<String, Object> selectStats() {
		return sqlSessionTemplate.selectOne(NS + "selectStats");
	}

	@Override
	public void insert(ProductVO product) {
		sqlSessionTemplate.insert(NS + "insert", product);
	}

	@Override
	public void update(ProductVO product) {
		sqlSessionTemplate.update(NS + "update", product);
	}

	@Override
	public void delete(String platform, String productId) {
		Map<String, Object> params = new HashMap<>();
		params.put("platform", platform);
		params.put("productId", productId);
		sqlSessionTemplate.delete(NS + "delete", params);
	}

	@Override
	public void recomputeStats(String platform, String productId) {
		Map<String, Object> params = new HashMap<>();
		params.put("platform", platform);
		params.put("productId", productId);
		sqlSessionTemplate.update(NS + "recomputeStats", params);
	}
}
