package kr.ac.kopo.member.service;

import java.util.ArrayList;
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

	/**
	 * 고유얼굴(Eigenfaces) 투영 공간에서의 유클리드 거리 임계값. 등록 얼굴이
	 * 2개 미만이라 PCA를 못 쓸 때는 원본 48x48 픽셀 벡터끼리 직접 비교하므로
	 * 스케일이 달라 별도 임계값을 둔다.
	 */
	private static final double EIGENFACE_MATCH_THRESHOLD = 2200.0;
	private static final double RAW_PIXEL_MATCH_THRESHOLD = 4000.0;
	private static final int EIGENFACE_COUNT = 10;

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

	/** descriptor 매개변수명은 그대로 두지만, 실제로는 48x48 흑백 픽셀 벡터(2304차원)다. */
	@Override
	public void registerFace(String memberId, double[] descriptor) {
		memberDAO.updateFaceDescriptor(memberId, toCsv(descriptor));
	}

	/**
	 * 등록된 모든 얼굴 픽셀 벡터로 고유얼굴(Eigenfaces) 기저를 직접 계산하고,
	 * 그 공간에 투영한 거리로 가장 가까운 회원을 찾는다. 등록자가 1명뿐이라
	 * PCA가 의미 없을 때는 원본 픽셀 벡터 거리로 대체한다.
	 */
	@Override
	public MemberVO loginWithFace(double[] descriptor) {
		List<MemberVO> candidates = memberDAO.selectAllWithFace();
		if (candidates.isEmpty()) {
			return null;
		}

		List<double[]> samples = new ArrayList<>();
		for (MemberVO c : candidates) {
			samples.add(fromCsv(c.getFaceDescriptor()));
		}

		EigenfaceRecognizer.Basis basis = EigenfaceRecognizer.computeEigenfaces(samples, EIGENFACE_COUNT);

		MemberVO best = null;
		double bestDistance = Double.MAX_VALUE;

		if (basis == null) {
			// 등록된 얼굴이 1개뿐이라 PCA를 계산할 수 없는 경우 — 원본 픽셀로 비교.
			for (int i = 0; i < candidates.size(); i++) {
				double distance = EigenfaceRecognizer.euclideanDistance(descriptor, samples.get(i));
				if (distance < bestDistance) {
					bestDistance = distance;
					best = candidates.get(i);
				}
			}
			if (best == null || bestDistance > RAW_PIXEL_MATCH_THRESHOLD) {
				return null;
			}
		} else {
			double[] queryWeights = EigenfaceRecognizer.project(descriptor, basis);
			for (int i = 0; i < candidates.size(); i++) {
				double[] candidateWeights = EigenfaceRecognizer.project(samples.get(i), basis);
				double distance = EigenfaceRecognizer.euclideanDistance(queryWeights, candidateWeights);
				if (distance < bestDistance) {
					bestDistance = distance;
					best = candidates.get(i);
				}
			}
			if (best == null || bestDistance > EIGENFACE_MATCH_THRESHOLD) {
				return null;
			}
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
}
