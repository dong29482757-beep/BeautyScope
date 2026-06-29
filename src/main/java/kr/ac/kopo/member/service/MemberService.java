package kr.ac.kopo.member.service;

import java.util.List;

import kr.ac.kopo.member.vo.MemberVO;

public interface MemberService {

	/** 회원가입. 아이디 중복이면 false. */
	boolean signup(MemberVO member);

	/** 로그인 성공 시 MemberVO, 실패(아이디 없음/비밀번호 불일치) 시 null. */
	MemberVO login(String memberId, String rawPassword);

	List<MemberVO> getList(String keyword);

	void changeRole(String memberId, String role);

	/** 소셜 로그인 콜백에서 호출 — 이미 가입돼 있으면 그 회원, 없으면 새로 만들어서 반환. */
	MemberVO findOrCreateSocialMember(String memberId, String nickname, String email, String joinType);

	/** 로그인한 회원의 얼굴 디스크립터(128차원)를 등록/갱신한다. */
	void registerFace(String memberId, double[] descriptor);

	/** 캡처한 디스크립터와 가장 가까운 등록된 얼굴을 찾는다. 임계값 안에 들어오는 회원이 없으면 null. */
	MemberVO loginWithFace(double[] descriptor);
}
