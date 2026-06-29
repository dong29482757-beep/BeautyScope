package kr.ac.kopo.reply.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.ac.kopo.reply.vo.ReplyVO;

@Repository
public class ReplyDAOImpl implements ReplyDAO {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public void insertReply(ReplyVO reply) {
		sqlSessionTemplate.insert("reply.dao.ReplyDAO.insertReply", reply);
	}

	@Override
	public List<ReplyVO> selectList(int boardNo) {
		return sqlSessionTemplate.selectList("reply.dao.ReplyDAO.selectList", boardNo);
	}
}
