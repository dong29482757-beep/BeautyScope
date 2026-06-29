package kr.ac.kopo.review.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ac.kopo.review.dao.ReviewDAO;
import kr.ac.kopo.review.vo.ReviewCriteria;
import kr.ac.kopo.review.vo.ReviewPageResult;
import kr.ac.kopo.review.vo.ReviewVO;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewDAO reviewDAO;

	@Override
	public ReviewPageResult getReviews(ReviewCriteria cri) {
		var list = reviewDAO.selectList(cri);
		int totalCount = reviewDAO.selectListCount(cri);
		return new ReviewPageResult(list, totalCount, cri.getPage(), cri.getPageSize());
	}

	@Override
	public void addReview(ReviewVO review) {
		reviewDAO.insert(review);
	}

	@Override
	public void deleteReview(long reviewNo) {
		reviewDAO.delete(reviewNo);
	}
}
