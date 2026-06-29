package kr.ac.kopo.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.ac.kopo.member.service.MemberService;
import kr.ac.kopo.member.vo.MemberVO;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	@GetMapping("/signup")
	public String signupForm() {
		return "member/signup";
	}

	@PostMapping("/signup")
	public String signup(MemberVO member, Model model, RedirectAttributes ra) {
		boolean ok = memberService.signup(member);
		if (!ok) {
			model.addAttribute("error", "이미 사용 중인 아이디예요.");
			model.addAttribute("member", member);
			return "member/signup";
		}
		ra.addAttribute("msg", "회원가입이 완료되었습니다. 로그인해주세요.");
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String loginForm() {
		return "member/login";
	}

	@PostMapping("/login")
	public String login(@RequestParam String memberId, @RequestParam String password,
			HttpSession session, Model model, RedirectAttributes ra) {
		MemberVO member = memberService.login(memberId, password);
		if (member == null) {
			model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않아요.");
			return "member/login";
		}
		session.setAttribute("loginMember", member);
		ra.addAttribute("msg", member.getNickname() + "님, 로그인이 완료되었습니다.");
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, RedirectAttributes ra) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		ra.addAttribute("msg", "로그아웃되었습니다.");
		return "redirect:/";
	}
}
