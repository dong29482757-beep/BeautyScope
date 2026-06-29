package kr.ac.kopo.aspect.vo;

public class RecommendVO {

	private String platform;
	private String productId;
	private String productName;
	private String brandName;
	private String category;
	private double avgRating;
	private int reviewCount;
	private double positiveRatio;
	private double overallRatio;
	private double score;
	private String matchedAspects;

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

	public double getPositiveRatio() {
		return positiveRatio;
	}

	public void setPositiveRatio(double positiveRatio) {
		this.positiveRatio = positiveRatio;
	}

	public double getOverallRatio() {
		return overallRatio;
	}

	public void setOverallRatio(double overallRatio) {
		this.overallRatio = overallRatio;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getMatchedAspects() {
		return matchedAspects;
	}

	public void setMatchedAspects(String matchedAspects) {
		this.matchedAspects = matchedAspects;
	}

	@Override
	public String toString() {
		return "RecommendVO [platform=" + platform + ", productId=" + productId + ", productName=" + productName
				+ ", brandName=" + brandName + ", category=" + category + ", avgRating=" + avgRating
				+ ", reviewCount=" + reviewCount + ", positiveRatio=" + positiveRatio + "]";
	}
}
