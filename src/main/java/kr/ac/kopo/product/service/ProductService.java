package kr.ac.kopo.product.service;

import java.util.List;
import java.util.Map;

import kr.ac.kopo.product.vo.BrandStatVO;
import kr.ac.kopo.product.vo.ProductCriteria;
import kr.ac.kopo.product.vo.ProductVO;
import kr.ac.kopo.product.vo.RatingDistVO;

public interface ProductService {

	List<ProductVO> getList(ProductCriteria cri);

	int getListCount(ProductCriteria cri);

	ProductVO getProduct(String platform, String productId);

	List<String> getCategories();

	List<ProductVO> getTop5();

	List<ProductVO> getRanking(String category);

	List<RatingDistVO> getRatingDist(String platform, String productId);

	List<BrandStatVO> getBrandStats(String category);

	Map<String, Object> getStats();

	void addProduct(ProductVO product);

	void updateProduct(ProductVO product);

	void deleteProduct(String platform, String productId);

	void recomputeStats(String platform, String productId);
}
