package kr.ac.kopo.product.dao;

import java.util.List;
import java.util.Map;

import kr.ac.kopo.product.vo.BrandStatVO;
import kr.ac.kopo.product.vo.ProductCriteria;
import kr.ac.kopo.product.vo.ProductVO;
import kr.ac.kopo.product.vo.RatingDistVO;

public interface ProductDAO {

	List<ProductVO> selectList(ProductCriteria cri);

	int selectListCount(ProductCriteria cri);

	ProductVO selectOne(String platform, String productId);

	List<String> selectCategories();

	List<ProductVO> selectTop5();

	List<ProductVO> selectRanking(String category);

	List<RatingDistVO> selectRatingDist(String platform, String productId);

	List<BrandStatVO> selectBrandStats(String category);

	Map<String, Object> selectStats();

	void insert(ProductVO product);

	void update(ProductVO product);

	void delete(String platform, String productId);

	/** 리뷰를 직접 추가/삭제한 뒤 t_product의 review_count/avg_rating/n_positive 등을 재집계한다. */
	void recomputeStats(String platform, String productId);
}
