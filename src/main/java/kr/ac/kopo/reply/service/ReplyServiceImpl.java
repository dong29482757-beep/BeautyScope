package kr.ac.kopo.reply.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ac.kopo.reply.dao.ReplyDAO;
import kr.ac.kopo.reply.vo.ReplyVO;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDAO replyDAO;

	@Override
	public void insertReply(ReplyVO reply) throws Exception {
		replyDAO.insertReply(reply);
	}

	@Override
	public List<ReplyVO> selectList(int boardNo) throws Exception {
		return replyDAO.selectList(boardNo);
	}
}
