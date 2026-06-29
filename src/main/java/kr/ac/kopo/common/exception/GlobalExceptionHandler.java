package kr.ac.kopo.common.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 컨트롤러에서 처리하지 못한 예외(잘못된 상품 ID, DB 오류 등)를 잡아서
 * 톰캣 기본 스택트레이스 화면 대신 안내 문구가 있는 에러 페이지로 보여준다.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, Model model) {
		ex.printStackTrace();
		model.addAttribute("errorMessage", "잠시 후 다시 시도해주세요. 문제가 계속되면 관리자에게 문의해주세요.");
		return "error/500";
	}
}
