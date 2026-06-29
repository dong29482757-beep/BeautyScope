package kr.ac.kopo.board.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.ac.kopo.board.vo.BoardVO;

@Repository
public class BoardDAOImpl implements BoardDAO {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public List<BoardVO> selectList() {
		return sqlSessionTemplate.selectList("board.dao.BoardDAO.selectList");
	}

	@Override
	public BoardVO selectOne(int no) {
		return sqlSessionTemplate.selectOne("board.dao.BoardDAO.selectOne", no);
	}

	@Override
	public void insertBoard(BoardVO board) {
		sqlSessionTemplate.insert("board.dao.BoardDAO.insertBoard", board);
	}

	@Override
	public void deleteBoard(int no) {
		sqlSessionTemplate.delete("board.dao.BoardDAO.deleteBoard", no);
	}

}
