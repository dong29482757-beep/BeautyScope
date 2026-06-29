package kr.ac.kopo.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.ac.kopo.member.service.MemberService;

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {

	@Autowired
	private MemberService memberService;

	@GetMapping
	public String list(String keyword, Model model) {
		model.addAttribute("memberList", memberService.getList(keyword));
		model.addAttribute("keyword", keyword);
		return "admin/members";
	}

	@PostMapping("/role")
	public String changeRole(@RequestParam String memberId, @RequestParam String role, RedirectAttributes ra) {
		memberService.changeRole(memberId, role);
		ra.addAttribute("msg", "회원 권한이 변경되었습니다.");
		return "redirect:/admin/members";
	}
}
