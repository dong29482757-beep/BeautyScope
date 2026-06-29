package kr.ac.kopo.member.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpSession;
import kr.ac.kopo.member.service.MemberService;
import kr.ac.kopo.member.vo.MemberVO;

/** 카카오/네이버 소셜 로그인. 일반 로그인 폼과 별개로 OAuth2 인가코드 흐름만 처리한다. */
@Controller
public class SocialLoginController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${kakao.client-id}")
	private String kakaoClientId;

	@Value("${kakao.redirect-uri}")
	private String kakaoRedirectUri;

	@Value("${naver.client-id}")
	private String naverClientId;

	@Value("${naver.client-secret}")
	private String naverClientSecret;

	@Value("${naver.redirect-uri}")
	private String naverRedirectUri;

	// ===== 카카오 =====

	@GetMapping("/login/kakao")
	public String kakaoLogin() {
		String url = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
				.queryParam("client_id", kakaoClientId)
				.queryParam("redirect_uri", kakaoRedirectUri)
				.queryParam("response_type", "code")
				.toUriString();
		return "redirect:" + url;
	}

	@GetMapping("/login/kakao/callback")
	public String kakaoCallback(@RequestParam String code, HttpSession session) {
		MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
		tokenParams.add("grant_type", "authorization_code");
		tokenParams.add("client_id", kakaoClientId);
		tokenParams.add("redirect_uri", kakaoRedirectUri);
		tokenParams.add("code", code);

		HttpHeaders tokenHeaders = new HttpHeaders();
		tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		Map<?, ?> tokenResult = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
				new HttpEntity<>(tokenParams, tokenHeaders), Map.class).getBody();
		String accessToken = (String) tokenResult.get("access_token");

		HttpHeaders profileHeaders = new HttpHeaders();
		profileHeaders.set("Authorization", "Bearer " + accessToken);
		Map<?, ?> profile = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET,
				new HttpEntity<>(profileHeaders), Map.class).getBody();

		long kakaoId = ((Number) profile.get("id")).longValue();
		Map<?, ?> kakaoAccount = (Map<?, ?>) profile.get("kakao_account");
		Map<?, ?> profileInfo = (kakaoAccount != null) ? (Map<?, ?>) kakaoAccount.get("profile") : null;
		String nickname = (profileInfo != null && profileInfo.get("nickname") != null)
				? (String) profileInfo.get("nickname") : "카카오회원";
		String email = (kakaoAccount != null) ? (String) kakaoAccount.get("email") : null;

		MemberVO member = memberService.findOrCreateSocialMember("kakao_" + kakaoId, nickname, email, "KAKAO");
		session.setAttribute("loginMember", member);
		return "redirect:/?msg=" + java.net.URLEncoder.encode(member.getNickname() + "님, 카카오 로그인이 완료되었습니다.", java.nio.charset.StandardCharsets.UTF_8);
	}

	// ===== 네이버 =====

	@GetMapping("/login/naver")
	public String naverLogin(HttpSession session) {
		String state = UUID.randomUUID().toString();
		session.setAttribute("naverState", state);
		String url = UriComponentsBuilder.fromHttpUrl("https://nid.naver.com/oauth2.0/authorize")
				.queryParam("response_type", "code")
				.queryParam("client_id", naverClientId)
				.queryParam("redirect_uri", naverRedirectUri)
				.queryParam("state", state)
				.toUriString();
		return "redirect:" + url;
	}

	@GetMapping("/login/naver/callback")
	public String naverCallback(@RequestParam String code, @RequestParam String state, HttpSession session) {
		Object savedState = session.getAttribute("naverState");
		if (savedState == null || !savedState.equals(state)) {
			return "redirect:/login";
		}

		Map<String, String> tokenParams = new HashMap<>();
		tokenParams.put("grant_type", "authorization_code");
		tokenParams.put("client_id", naverClientId);
		tokenParams.put("client_secret", naverClientSecret);
		tokenParams.put("code", code);
		tokenParams.put("state", state);

		String tokenUrl = UriComponentsBuilder.fromHttpUrl("https://nid.naver.com/oauth2.0/token")
				.queryParams(toMultiValueMap(tokenParams))
				.toUriString();
		Map<?, ?> tokenResult = restTemplate.getForObject(tokenUrl, Map.class);
		String accessToken = (String) tokenResult.get("access_token");

		HttpHeaders profileHeaders = new HttpHeaders();
		profileHeaders.set("Authorization", "Bearer " + accessToken);
		Map<?, ?> response = restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET,
				new HttpEntity<>(profileHeaders), Map.class).getBody();
		Map<?, ?> naverAccount = (Map<?, ?>) response.get("response");

		String naverId = (String) naverAccount.get("id");
		String nickname = (naverAccount.get("nickname") != null) ? (String) naverAccount.get("nickname") : "네이버회원";
		String email = (String) naverAccount.get("email");

		MemberVO member = memberService.findOrCreateSocialMember("naver_" + naverId, nickname, email, "NAVER");
		session.setAttribute("loginMember", member);
		return "redirect:/?msg=" + java.net.URLEncoder.encode(member.getNickname() + "님, 네이버 로그인이 완료되었습니다.", java.nio.charset.StandardCharsets.UTF_8);
	}

	private MultiValueMap<String, String> toMultiValueMap(Map<String, String> map) {
		MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
		map.forEach(result::add);
		return result;
	}
}
