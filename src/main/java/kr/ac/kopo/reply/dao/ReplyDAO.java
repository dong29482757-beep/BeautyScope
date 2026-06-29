package kr.ac.kopo.reply.dao;

import java.util.List;

import kr.ac.kopo.reply.vo.ReplyVO;

public interface ReplyDAO {

	void insertReply(ReplyVO reply);

	List<ReplyVO> selectList(int boardNo);
}
