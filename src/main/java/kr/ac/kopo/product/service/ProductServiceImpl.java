package kr.ac.kopo.product.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ac.kopo.product.dao.ProductDAO;
import kr.ac.kopo.product.vo.BrandStatVO;
import kr.ac.kopo.product.vo.ProductCriteria;
import kr.ac.kopo.product.vo.ProductVO;
import kr.ac.kopo.product.vo.RatingDistVO;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;

	@Override
	public List<ProductVO> getList(ProductCriteria cri) {
		return productDAO.selectList(cri);
	}

	@Override
	public int getListCount(ProductCriteria cri) {
		return productDAO.selectListCount(cri);
	}

	@Override
	public ProductVO getProduct(String platform, String productId) {
		return productDAO.selectOne(platform, productId);
	}

	@Override
	public List<String> getCategories() {
		return productDAO.selectCategories();
	}

	@Override
	public List<ProductVO> getTop5() {
		return productDAO.selectTop5();
	}

	@Override
	public List<ProductVO> getRanking(String category) {
		return productDAO.selectRanking(category);
	}

	@Override
	public List<RatingDistVO> getRatingDist(String platform, String productId) {
		return productDAO.selectRatingDist(platform, productId);
	}

	@Override
	public List<BrandStatVO> getBrandStats(String category) {
		return productDAO.selectBrandStats(category);
	}

	@Override
	public Map<String, Object> getStats() {
		return productDAO.selectStats();
	}

	@Override
	public void addProduct(ProductVO product) {
		productDAO.insert(product);
	}

	@Override
	public void updateProduct(ProductVO product) {
		productDAO.update(product);
	}

	@Override
	public void deleteProduct(String platform, String productId) {
		productDAO.delete(platform, productId);
	}

	@Override
	public void recomputeStats(String platform, String productId) {
		productDAO.recomputeStats(platform, productId);
	}
}
