package kr.ac.kopo.review.service;

import kr.ac.kopo.review.vo.ReviewCriteria;
import kr.ac.kopo.review.vo.ReviewPageResult;
import kr.ac.kopo.review.vo.ReviewVO;

public interface ReviewService {

	ReviewPageResult getReviews(ReviewCriteria cri);

	void addReview(ReviewVO review);

	void deleteReview(long reviewNo);
}
