package kr.ac.kopo.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.ac.kopo.product.service.ProductService;
import kr.ac.kopo.review.service.ReviewService;
import kr.ac.kopo.review.vo.ReviewCriteria;
import kr.ac.kopo.review.vo.ReviewVO;

@Controller
@RequestMapping("/admin/products/{platform}/{productId}/reviews")
public class AdminReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ProductService productService;

	@GetMapping
	public String list(@PathVariable String platform, @PathVariable String productId, Model model) {
		ReviewCriteria cri = new ReviewCriteria();
		cri.setPlatform(platform);
		cri.setProductId(productId);
		cri.setPageSize(50);

		model.addAttribute("product", productService.getProduct(platform, productId));
		model.addAttribute("reviewResult", reviewService.getReviews(cri));
		return "admin/reviews";
	}

	@PostMapping("/new")
	public String create(@PathVariable String platform, @PathVariable String productId, ReviewVO review,
			RedirectAttributes ra) {
		review.setPlatform(platform);
		review.setProductId(productId);
		reviewService.addReview(review);
		productService.recomputeStats(platform, productId);
		ra.addAttribute("msg", "리뷰가 등록되었습니다.");
		return "redirect:/admin/products/" + platform + "/" + productId + "/reviews";
	}

	@PostMapping("/{reviewNo}/delete")
	public String delete(@PathVariable String platform, @PathVariable String productId,
			@PathVariable long reviewNo, RedirectAttributes ra) {
		reviewService.deleteReview(reviewNo);
		productService.recomputeStats(platform, productId);
		ra.addAttribute("msg", "리뷰가 삭제되었습니다.");
		return "redirect:/admin/products/" + platform + "/" + productId + "/reviews";
	}
}
