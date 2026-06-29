package kr.ac.kopo.member.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.ac.kopo.member.dao.MemberDAO;
import kr.ac.kopo.member.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	/** face-api.js의 FaceMatcher 기본 임계값(유클리드 거리)과 동일하게 맞춤 — 데모용 정확도. */
	private static final double FACE_MATCH_THRESHOLD = 0.6;

	@Autowired
	private MemberDAO memberDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public boolean signup(MemberVO member) {
		if (memberDAO.selectCount(member.getMemberId()) > 0) {
			return false;
		}
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		if (member.getRole() == null) {
			member.setRole("USER");
		}
		if (member.getJoinType() == null) {
			member.setJoinType("LOCAL");
		}
		memberDAO.insert(member);
		return true;
	}

	@Override
	public MemberVO login(String memberId, String rawPassword) {
		MemberVO member = memberDAO.selectOne(memberId);
		if (member == null) {
			return null;
		}
		if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
			return null;
		}
		return member;
	}

	@Override
	public List<MemberVO> getList(String keyword) {
		return memberDAO.selectList(keyword);
	}

	@Override
	public void changeRole(String memberId, String role) {
		memberDAO.updateRole(memberId, role);
	}

	@Override
	public MemberVO findOrCreateSocialMember(String memberId, String nickname, String email, String joinType) {
		MemberVO member = memberDAO.selectOne(memberId);
		if (member != null) {
			return member;
		}
		member = new MemberVO();
		member.setMemberId(memberId);
		// 소셜 로그인은 비밀번호로 로그인하지 않으니 추측 불가능한 랜덤 값을 해시해서 NOT NULL만 채워둔다.
		member.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
		member.setNickname(nickname);
		member.setEmail(email);
		member.setRole("USER");
		member.setJoinType(joinType);
		memberDAO.insert(member);
		return member;
	}

	@Override
	public void registerFace(String memberId, double[] descriptor) {
		memberDAO.updateFaceDescriptor(memberId, toCsv(descriptor));
	}

	@Override
	public MemberVO loginWithFace(double[] descriptor) {
		MemberVO best = null;
		double bestDistance = Double.MAX_VALUE;

		for (MemberVO candidate : memberDAO.selectAllWithFace()) {
			double[] saved = fromCsv(candidate.getFaceDescriptor());
			double distance = euclideanDistance(descriptor, saved);
			if (distance < bestDistance) {
				bestDistance = distance;
				best = candidate;
			}
		}

		if (best == null || bestDistance > FACE_MATCH_THRESHOLD) {
			return null;
		}
		return memberDAO.selectOne(best.getMemberId());
	}

	private String toCsv(double[] descriptor) {
		return Arrays.stream(descriptor).mapToObj(String::valueOf).collect(Collectors.joining(","));
	}

	private double[] fromCsv(String csv) {
		String[] parts = csv.split(",");
		double[] result = new double[parts.length];
		for (int i = 0; i < parts.length; i++) {
			result[i] = Double.parseDouble(parts[i]);
		}
		return result;
	}

	private double euclideanDistance(double[] a, double[] b) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			double diff = a[i] - b[i];
			sum += diff * diff;
		}
		return Math.sqrt(sum);
	}
}
