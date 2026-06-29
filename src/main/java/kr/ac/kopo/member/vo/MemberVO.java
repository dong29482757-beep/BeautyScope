package kr.ac.kopo.member.vo;

import java.util.Date;

public class MemberVO {

	private String memberId;
	private String password;
	private String nickname;
	private String email;
	private String role;
	private String joinType;
	private Date regDate;
	private String faceDescriptor; // 128차원 얼굴 디스크립터를 콤마로 이어붙인 문자열, 미등록이면 null

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getJoinType() {
		return joinType;
	}

	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getFaceDescriptor() {
		return faceDescriptor;
	}

	public void setFaceDescriptor(String faceDescriptor) {
		this.faceDescriptor = faceDescriptor;
	}

	public boolean isAdmin() {
		return "ADMIN".equals(role);
	}

	@Override
	public String toString() {
		return "MemberVO [memberId=" + memberId + ", nickname=" + nickname + ", email=" + email + ", role=" + role
				+ ", joinType=" + joinType + ", regDate=" + regDate + "]";
	}
}
