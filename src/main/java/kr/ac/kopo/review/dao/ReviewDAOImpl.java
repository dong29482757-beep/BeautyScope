package kr.ac.kopo.review.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.ac.kopo.review.vo.ReviewCriteria;
import kr.ac.kopo.review.vo.ReviewVO;

@Repository
public class ReviewDAOImpl implements ReviewDAO {

	private static final String NS = "review.dao.ReviewDAO.";

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<ReviewVO> selectList(ReviewCriteria cri) {
		return sqlSessionTemplate.selectList(NS + "selectList", cri);
	}

	@Override
	public int selectListCount(ReviewCriteria cri) {
		return sqlSessionTemplate.selectOne(NS + "selectListCount", cri);
	}

	@Override
	public void insert(ReviewVO review) {
		sqlSessionTemplate.insert(NS + "insert", review);
	}

	@Override
	public void delete(long reviewNo) {
		sqlSessionTemplate.delete(NS + "delete", reviewNo);
	}
}
