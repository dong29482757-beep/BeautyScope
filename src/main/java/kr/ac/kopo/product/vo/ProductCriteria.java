package kr.ac.kopo.product.vo;

/**
 * 제품 목록/검색 조건 + 페이징 파라미터를 한 번에 묶어서 MyBatis로 넘기는 VO.
 * V1-201(카테고리 필터), V1-202/203(정렬), V1-204(페이징), V1-401(검색)에 대응.
 */
public class ProductCriteria {

	private String category;     // null/빈값이면 전체
	private String keyword;      // 제품명 검색 키워드 (null/빈값이면 검색 안 함)
	// "rating_desc"(평점높은순, 기본) | "rating_asc"(평점낮은순) | "latest"(최신순, 최근 리뷰 등록일 기준)
	private String sort = "rating_desc";
	private int page = 1;
	private int pageSize = 20;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
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
