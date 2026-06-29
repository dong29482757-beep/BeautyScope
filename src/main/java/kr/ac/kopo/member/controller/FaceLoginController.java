package kr.ac.kopo.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.ac.kopo.member.service.MemberService;
import kr.ac.kopo.member.vo.MemberVO;

/** 웹캠으로 캡처한 얼굴 디스크립터(128차원)를 등록/대조한다. 실제 인증 등급 보안이 아니라 데모용. */
@Controller
public class FaceLoginController {

	@Autowired
	private MemberService memberService;

	@GetMapping("/face/register")
	public String registerForm(HttpSession session) {
		if (session.getAttribute("loginMember") == null) {
			return "redirect:/login";
		}
		return "face/register";
	}

	@PostMapping("/face/register")
	@ResponseBody
	public Map<String, Object> register(@RequestBody double[] descriptor, HttpSession session) {
		MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
		Map<String, Object> result = new HashMap<>();
		if (loginMember == null) {
			result.put("success", false);
			result.put("message", "로그인이 필요합니다.");
			return result;
		}
		memberService.registerFace(loginMember.getMemberId(), descriptor);
		result.put("success", true);
		return result;
	}

	@GetMapping("/login/face")
	public String loginForm() {
		return "face/login";
	}

	@PostMapping("/login/face")
	@ResponseBody
	public Map<String, Object> loginWithFace(@RequestBody double[] descriptor, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		MemberVO matched = memberService.loginWithFace(descriptor);
		if (matched == null) {
			result.put("success", false);
			result.put("message", "일치하는 얼굴을 찾지 못했어요.");
			return result;
		}
		session.setAttribute("loginMember", matched);
		result.put("success", true);
		return result;
	}
}
