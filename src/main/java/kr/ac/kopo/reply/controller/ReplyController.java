package kr.ac.kopo.reply.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.kopo.reply.service.ReplyService;
import kr.ac.kopo.reply.vo.ReplyVO;

@RestController
public class ReplyController {

	@Autowired
	private ReplyService replyService;

	@GetMapping("/reply/{boardNo}")
	public List<ReplyVO> getReplyList(@PathVariable("boardNo") int boardNo) throws Exception {
		return replyService.selectList(boardNo);
	}

	@PostMapping("/reply/{boardNo}")
	public String addReply(@PathVariable("boardNo") int boardNo, ReplyVO reply) throws Exception {
		reply.setBoardNo(boardNo);
		replyService.insertReply(reply);
		return "ok";
	}
}
