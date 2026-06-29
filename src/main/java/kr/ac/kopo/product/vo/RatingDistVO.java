package kr.ac.kopo.product.vo;

/** 제품 상세 페이지의 별점 분포 차트(V1-302)용 집계 행. */
public class RatingDistVO {

	private int rating;
	private int cnt;

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
}
