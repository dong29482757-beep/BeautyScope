package kr.ac.kopo.aspect.vo;

public class RecommendCandidateVO {

	private String platform;
	private String productId;
	private String productName;
	private String brandName;
	private double avgRating;
	private int reviewCount;
	private int nPositive;
	private String aspect;
	private int posCnt;
	private int totalCnt;

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

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public int getnPositive() {
		return nPositive;
	}

	public void setnPositive(int nPositive) {
		this.nPositive = nPositive;
	}

	public String getAspect() {
		return aspect;
	}

	public void setAspect(String aspect) {
		this.aspect = aspect;
	}

	public int getPosCnt() {
		return posCnt;
	}

	public void setPosCnt(int posCnt) {
		this.posCnt = posCnt;
	}

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
}
