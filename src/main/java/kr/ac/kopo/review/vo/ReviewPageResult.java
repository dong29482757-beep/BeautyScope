package kr.ac.kopo.review.vo;

import java.util.List;

/** fetch()로 받아갈 JSON 응답 형태 (리뷰 목록 + 페이징 정보). */
public class ReviewPageResult {

	private List<ReviewVO> reviews;
	private int totalCount;
	private int totalPages;
	private int page;

	public ReviewPageResult(List<ReviewVO> reviews, int totalCount, int page, int pageSize) {
		this.reviews = reviews;
		this.totalCount = totalCount;
		this.page = page;
		this.totalPages = Math.max((int) Math.ceil(totalCount / (double) pageSize), 1);
	}

	public List<ReviewVO> getReviews() {
		return reviews;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getPage() {
		return page;
	}
}
