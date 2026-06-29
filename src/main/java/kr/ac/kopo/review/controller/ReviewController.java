package kr.ac.kopo.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.kopo.review.service.ReviewService;
import kr.ac.kopo.review.vo.ReviewCriteria;
import kr.ac.kopo.review.vo.ReviewPageResult;

/**
 * 제품 상세 페이지에서 fetch()로 호출하는 비동기 리뷰 API.
 * V1-303(리뷰 목록 비동기 로딩), V1-402(리뷰 내용 검색)에 대응.
 */
@RestController
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@GetMapping("/api/reviews")
	public ReviewPageResult list(ReviewCriteria cri) {
		return reviewService.getReviews(cri);
	}
}
