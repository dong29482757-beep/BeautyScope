package kr.ac.kopo.reply.service;

import java.util.List;

import kr.ac.kopo.reply.vo.ReplyVO;

public interface ReplyService {

	void insertReply(ReplyVO reply) throws Exception;

	List<ReplyVO> selectList(int boardNo) throws Exception;
}
