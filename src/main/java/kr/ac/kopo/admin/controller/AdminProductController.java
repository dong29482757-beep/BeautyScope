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
import kr.ac.kopo.product.vo.ProductCriteria;
import kr.ac.kopo.product.vo.ProductVO;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public String list(ProductCriteria cri, Model model) {
		var list = productService.getList(cri);
		int totalCount = productService.getListCount(cri);
		int totalPages = (int) Math.ceil(totalCount / (double) cri.getPageSize());

		model.addAttribute("productList", list);
		model.addAttribute("cri", cri);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPages", Math.max(totalPages, 1));
		return "admin/products";
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		model.addAttribute("categories", productService.getCategories());
		return "admin/productForm";
	}

	@PostMapping("/new")
	public String create(ProductVO product, RedirectAttributes ra) {
		productService.addProduct(product);
		ra.addAttribute("msg", "상품이 등록되었습니다.");
		return "redirect:/admin/products";
	}

	@GetMapping("/{platform}/{productId}/edit")
	public String editForm(@PathVariable String platform, @PathVariable String productId, Model model) {
		model.addAttribute("product", productService.getProduct(platform, productId));
		model.addAttribute("categories", productService.getCategories());
		return "admin/productForm";
	}

	@PostMapping("/{platform}/{productId}/edit")
	public String update(@PathVariable String platform, @PathVariable String productId, ProductVO product,
			RedirectAttributes ra) {
		product.setPlatform(platform);
		product.setProductId(productId);
		productService.updateProduct(product);
		ra.addAttribute("msg", "상품 정보가 수정되었습니다.");
		return "redirect:/admin/products";
	}

	@PostMapping("/{platform}/{productId}/delete")
	public String delete(@PathVariable String platform, @PathVariable String productId, RedirectAttributes ra) {
		productService.deleteProduct(platform, productId);
		ra.addAttribute("msg", "상품이 삭제되었습니다.");
		return "redirect:/admin/products";
	}
}
