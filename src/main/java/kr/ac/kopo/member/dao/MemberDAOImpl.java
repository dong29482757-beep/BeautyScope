package kr.ac.kopo.member.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.ac.kopo.member.vo.MemberVO;

@Repository
public class MemberDAOImpl implements MemberDAO {

	private static final String NS = "member.dao.MemberDAO.";

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public void insert(MemberVO member) {
		sqlSessionTemplate.insert(NS + "insert", member);
	}

	@Override
	public MemberVO selectOne(String memberId) {
		return sqlSessionTemplate.selectOne(NS + "selectOne", memberId);
	}

	@Override
	public List<MemberVO> selectList(String keyword) {
		return sqlSessionTemplate.selectList(NS + "selectList", keyword);
	}

	@Override
	public int selectCount(String memberId) {
		return sqlSessionTemplate.selectOne(NS + "selectCount", memberId);
	}

	@Override
	public void updateRole(String memberId, String role) {
		Map<String, Object> params = new HashMap<>();
		params.put("memberId", memberId);
		params.put("role", role);
		sqlSessionTemplate.update(NS + "updateRole", params);
	}

	@Override
	public void updateFaceDescriptor(String memberId, String faceDescriptor) {
		Map<String, Object> params = new HashMap<>();
		params.put("memberId", memberId);
		params.put("faceDescriptor", faceDescriptor);
		sqlSessionTemplate.update(NS + "updateFaceDescriptor", params);
	}

	@Override
	public List<MemberVO> selectAllWithFace() {
		return sqlSessionTemplate.selectList(NS + "selectAllWithFace");
	}
}
