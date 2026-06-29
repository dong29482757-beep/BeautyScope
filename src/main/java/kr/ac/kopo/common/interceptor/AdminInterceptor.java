package kr.ac.kopo.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.ac.kopo.member.vo.MemberVO;

/** /admin/** 경로는 로그인한 ADMIN 권한 회원만 접근 가능하게 막는다. */
public class AdminInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		MemberVO loginMember = (session != null) ? (MemberVO) session.getAttribute("loginMember") : null;

		if (loginMember == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
		if (!loginMember.isAdmin()) {
			response.sendRedirect(request.getContextPath() + "/");
			return false;
		}
		return true;
	}
}
