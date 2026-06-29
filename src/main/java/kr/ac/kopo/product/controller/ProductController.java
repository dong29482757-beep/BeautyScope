package kr.ac.kopo.product.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.ac.kopo.aspect.service.AspectService;
import kr.ac.kopo.aspect.vo.AspectVO;
import kr.ac.kopo.product.service.ProductService;
import kr.ac.kopo.product.vo.ProductCriteria;
import kr.ac.kopo.product.vo.ProductVO;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private AspectService aspectService;

	/**
	 * 제품 목록. 카테고리 필터(V1-201) + 정렬(V1-202/203) + 페이징(V1-204) +
	 * 제품명 키워드 검색(V1-401)을 한 화면에서 같이 처리한다.
	 */
	@GetMapping("/product")
	public String list(ProductCriteria cri, Model model) {
		var list = productService.getList(cri);
		int totalCount = productService.getListCount(cri);
		int totalPages = (int) Math.ceil(totalCount / (double) cri.getPageSize());

		model.addAttribute("productList", list);
		model.addAttribute("cri", cri);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPages", Math.max(totalPages, 1));
		model.addAttribute("categories", productService.getCategories());
		return "product/list";
	}

	/** 제품 상세: 제품 정보(V1-301) + 별점분포(V1-302, 차트 데이터는 모델로 전달). 리뷰목록(V1-303)은 fetch()로 별도 비동기 호출. */
	@GetMapping("/product/{platform}/{productId}")
	public String detail(@PathVariable("platform") String platform,
			@PathVariable("productId") String productId, Model model) {
		ProductVO product = productService.getProduct(platform, productId);
		model.addAttribute("product", product);
		model.addAttribute("ratingDist", productService.getRatingDist(platform, productId));
		List<AspectVO> aspects = aspectService.getAspects(platform, productId);
		model.addAttribute("aspects", aspects);
		// 장점은 "긍정 리뷰"에서, 단점은 "부정 리뷰"에서 많이 언급된 속성 순으로 각각 뽑는다 (긍정비율 50% 기준 양분 X)
		model.addAttribute("prosAspects", aspects.stream()
				.filter(a -> a.getPositiveCnt() > 0)
				.sorted(Comparator.comparingInt(AspectVO::getPositiveCnt).reversed())
				.toList());
		model.addAttribute("consAspects", aspects.stream()
				.filter(a -> a.getNegativeCnt() > 0)
				.sorted(Comparator.comparingInt(AspectVO::getNegativeCnt).reversed())
				.toList());
		return "product/detail";
	}

	/** 카테고리 랭킹(V1-501) + 브랜드 비교 차트(V1-502). */
	@GetMapping("/ranking")
	public String ranking(String category, Model model) {
		var categories = productService.getCategories();
		String selected = (category == null || category.isBlank()) ? categories.get(0) : category;

		model.addAttribute("categories", categories);
		model.addAttribute("selectedCategory", selected);
		model.addAttribute("rankingList", productService.getRanking(selected));
		model.addAttribute("brandStats", productService.getBrandStats(selected));
		return "ranking/list";
	}
}
