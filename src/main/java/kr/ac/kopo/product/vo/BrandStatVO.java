package kr.ac.kopo.product.vo;

/** 브랜드별 평균 평점 비교 차트(V1-502)용 집계 행. */
public class BrandStatVO {

	private String brandName;
	private double avgRating;
	private int productCount;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
}
