package kr.ac.kopo.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ac.kopo.board.dao.BoardDAO;
import kr.ac.kopo.board.vo.BoardVO;

@Service
public class BoardService {

	@Autowired
	private BoardDAO boardDAO;
	
	public List<BoardVO> selectAllBoard() {
		return boardDAO.selectList();
	}

	public BoardVO selectBoard(int no) {
		return boardDAO.selectOne(no);
	}

	public void insertBoard(BoardVO board) {
		boardDAO.insertBoard(board);
	}

	public void deleteBoard(int no) {
		boardDAO.deleteBoard(no);
	}
}
