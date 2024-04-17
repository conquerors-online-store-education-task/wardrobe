package bg.conquerors.wardrobe.controller;

import bg.conquerors.wardrobe.model.dto.AddProductDTO;
import bg.conquerors.wardrobe.model.enums.CategoryEnum;
import bg.conquerors.wardrobe.model.enums.GenderEnum;
import bg.conquerors.wardrobe.model.enums.SizeEnum;
import bg.conquerors.wardrobe.model.enums.StyleEnum;
import bg.conquerors.wardrobe.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/shop")
    public String getViewProducts(Model model) {
        model.addAttribute("products", productService.getViewOfProducts());

        return "shop";
    }

//    @GetMapping("/product-detail/{id}")
//    public String productDetail(@PathVariable("id") Long id, Model model) {
//
//        return "product-detail";
//    }
   @GetMapping("/shop/product-detail/{productNumber}")
    public String viewProduct(Model model, @PathVariable String productNumber) {

        model.addAttribute("product", productService.getProductByProductNumber(productNumber));
        model.addAttribute("sizes", SizeEnum.values());

        return "product-detail";
    }
}
