package kr.ac.kopo.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.ac.kopo.product.service.ProductService;

@Controller
public class MainController {

	@Autowired
	private ProductService productService;

	@GetMapping("/")
	public String main(Model model) {
		model.addAttribute("top5", productService.getTop5());             // V1-101
		model.addAttribute("categories", productService.getCategories()); // V1-102
		model.addAttribute("stats", productService.getStats());
		return "index";
	}
}
