package kr.ac.kopo.aspect.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.ac.kopo.aspect.service.AspectService;
import kr.ac.kopo.product.service.ProductService;

/**
 * 속성기반(ABSA) 분석 데이터를 이용한 합성 점수 추천.
 * 카테고리 + 신경쓰는 속성(복수 선택 가능)을 고르면, 선택한 속성들의
 * 만족도(베이지안 보정) + 전체 리뷰 만족도 + 평점을 가중합한 점수로
 * 제품을 정렬해 보여준다. 외부 AI API 호출 없이 우리가 이미 분석해 둔
 * t_aspect_sentiment / t_product 데이터로만 점수를 계산한다.
 */
@Controller
@RequestMapping("/recommend")
public class RecommendController {

	@Autowired
	private AspectService aspectService;

	@Autowired
	private ProductService productService;

	@GetMapping
	public String form(String category, String[] aspects, Model model) {
		model.addAttribute("categories", productService.getCategories());
		model.addAttribute("aspectNames", aspectService.getAspectNames());

		if (category != null && !category.isBlank() && aspects != null && aspects.length > 0) {
			List<String> aspectList = Arrays.asList(aspects);
			model.addAttribute("selectedCategory", category);
			model.addAttribute("selectedAspects", aspectList);
			model.addAttribute("result", aspectService.getRecommend(category, aspectList));
		}
		return "recommend/form";
	}
}
