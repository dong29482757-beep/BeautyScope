package kr.ac.kopo.review.dao;

import java.util.List;

import kr.ac.kopo.review.vo.ReviewCriteria;
import kr.ac.kopo.review.vo.ReviewVO;

public interface ReviewDAO {

	List<ReviewVO> selectList(ReviewCriteria cri);

	int selectListCount(ReviewCriteria cri);

	void insert(ReviewVO review);

	void delete(long reviewNo);
}
