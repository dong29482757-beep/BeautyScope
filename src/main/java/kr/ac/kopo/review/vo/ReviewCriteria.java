package kr.ac.kopo.review.vo;

/** 제품 상세 페이지의 비동기 리뷰 목록(V1-303) + 리뷰내용 검색(V1-402) 조건. */
public class ReviewCriteria {

	private String platform;
	private String productId;
	private String keyword; // null/빈값이면 검색 안 함, review_content LIKE 검색
	private int page = 1;
	private int pageSize = 10;

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getOffset() {
		return (page - 1) * pageSize;
	}
}
