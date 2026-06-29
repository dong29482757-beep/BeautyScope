package kr.ac.kopo.member.dao;

import java.util.List;

import kr.ac.kopo.member.vo.MemberVO;

public interface MemberDAO {

	void insert(MemberVO member);

	MemberVO selectOne(String memberId);

	List<MemberVO> selectList(String keyword);

	int selectCount(String memberId);

	void updateRole(String memberId, String role);

	void updateFaceDescriptor(String memberId, String faceDescriptor);

	/** 얼굴이 등록된(face_descriptor IS NOT NULL) 회원만 가져온다 (로그인 시 매칭 대상). */
	List<MemberVO> selectAllWithFace();
}
