package kr.ac.kopo.board.dao;

import java.util.List;

import kr.ac.kopo.board.vo.BoardVO;

public interface BoardDAO {

	List<BoardVO> selectList();

	BoardVO selectOne(int no);

	void insertBoard(BoardVO board);

	void deleteBoard(int no);
}
