package kr.ac.kopo.product.vo;

public class ProductVO {

	private String platform;
	private String productId;
	private String productName;
	private String brandName;
	private String category;
	private int reviewCount;
	private double avgRating;
	private int nNegative;
	private int nNeutral;
	private int nPositive;
	private String imagePath; // resources/product_images/{platform}/{file} 상대경로, 없으면 null

	public ProductVO() {
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}

	public int getnNegative() {
		return nNegative;
	}

	public void setnNegative(int nNegative) {
		this.nNegative = nNegative;
	}

	public int getnNeutral() {
		return nNeutral;
	}

	public void setnNeutral(int nNeutral) {
		this.nNeutral = nNeutral;
	}

	public int getnPositive() {
		return nPositive;
	}

	public void setnPositive(int nPositive) {
		this.nPositive = nPositive;
	}

	@Override
	public String toString() {
		return "ProductVO [platform=" + platform + ", productId=" + productId + ", productName=" + productName
				+ ", brandName=" + brandName + ", category=" + category + ", reviewCount=" + reviewCount
				+ ", avgRating=" + avgRating + "]";
	}
}
