package kr.ac.kopo.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.ac.kopo.board.service.BoardService;
import kr.ac.kopo.board.vo.BoardVO;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;

	@GetMapping("/board")
	public String listBoard(Model model) throws Exception {
		List<BoardVO> boardList = boardService.selectAllBoard();
		model.addAttribute("boardList", boardList);
		return "board/list";
	}

	@GetMapping("/board/{no}")
	public String detailBoard(@PathVariable("no") int no, Model model) throws Exception {
		BoardVO board = boardService.selectBoard(no);
		model.addAttribute("board", board);
		return "board/detail";
	}

	@GetMapping("/board/write")
	public String writeForm() {
		return "board/write";
	}

	@PostMapping("/board/write")
	public String write(BoardVO board) throws Exception {
		boardService.insertBoard(board);
		return "redirect:/board";
	}

	@PostMapping("/board/delete")
	public String delete(@RequestParam("no") int no) throws Exception {
		boardService.deleteBoard(no);
		return "redirect:/board";
	}
}
