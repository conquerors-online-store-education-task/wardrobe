package bg.conquerors.wardrobe.controller;

import bg.conquerors.wardrobe.model.dto.AddDiscountDTO;
import bg.conquerors.wardrobe.model.dto.AddOrderDTO;
import bg.conquerors.wardrobe.model.dto.AddProductDTO;
import bg.conquerors.wardrobe.model.entity.Product;
import bg.conquerors.wardrobe.model.enums.*;
import bg.conquerors.wardrobe.service.AdminService;
import bg.conquerors.wardrobe.service.ProductService;
import jakarta.validation.Valid;
import lombok.Data;
import org.hibernate.annotations.processing.Find;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;
    private final ProductService productService;


    public AdminController(AdminService adminService, ProductService productService) {
        this.adminService = adminService;
        this.productService = productService;
    }


    @GetMapping("")
    public String admin(Model model) {
        model.addAttribute("products",productService.getViewOfProducts());
        return "admin/admin";
    }

    //region <Products CRUD>
    @GetMapping("/add-product")
    public String addProduct(Model model) {

        AddProductDTO addProductDTO = new AddProductDTO();
        Map<String,Integer> quantity = new Hashtable<>();

        for (SizeEnum sizeEnum : SizeEnum.values()){
            quantity.put(sizeEnum.toString(),0);
        }

        addProductDTO.setQuantities(quantity);

        model.addAttribute("addProductDTO", addProductDTO);
        model.addAttribute("categoryOptions", CategoryEnum.values());
        model.addAttribute("genderOptions", GenderEnum.values());
        model.addAttribute("sizeOptions", SizeEnum.values());
        model.addAttribute("styleOptions", StyleEnum.values());

        return "admin/product/add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(AddProductDTO addProductDTO, BindingResult result) {

        if (result.hasErrors()) {

            return "error";
        }

        adminService.addProduct(addProductDTO);

        return "redirect:/admin";
    }

    @GetMapping("/edit-product/{productNumber}")
    public String editProduct(@PathVariable("productNumber") String productNumber, Model model) {

        AddProductDTO addProductDTO = adminService.getProductByProductNumber(productNumber);


        if (addProductDTO == null)
            return "error";

        model.addAttribute("addProductDTO", addProductDTO);
        model.addAttribute("productId", productNumber);
        model.addAttribute("categoryOptions", CategoryEnum.values());
        model.addAttribute("genderOptions", GenderEnum.values());
        model.addAttribute("sizeOptions", SizeEnum.values());
        model.addAttribute("styleOptions", StyleEnum.values());

        return "admin/product/edit-product";
    }

    @PostMapping("/edit-product/{productNumber}")
    public String editProduct(@PathVariable("productNumber") String productNumber,AddProductDTO addProductDTO) {

        adminService.editProduct(productNumber, addProductDTO);
        return "redirect:/admin";
    }

    @GetMapping("/delete-product/{productNumber}")
    public String deleteProductGet(@PathVariable("productNumber") String productNumber) {

        adminService.deleteProduct(productNumber);

        return "redirect:/admin";
    }

    @PostMapping("/delete-product/{productNumber}")
    public String deleteProductPost(@PathVariable("productNumber") String productNumber) {

        adminService.deleteProduct(productNumber);

        return "redirect:/admin";
    }

    @GetMapping("/product-grid")
    public String productGrid(Model model) {

        model.addAttribute("products", productService.getViewOfProducts());

        return "admin/product-grid";
    }
    //endregion

    //region <Orders CRUD>

    @GetMapping("/add-order")
    public String addOrder(Model model) {

        model.addAttribute("addOrderDTO", new AddOrderDTO());
        model.addAttribute("orderStatusOptions", OrderStatusEnum.values());

        return "admin/order/add-order";
    }

    @PostMapping("/add-order")
    public String addOrder(AddOrderDTO addOrderDTO) {

        adminService.addOrder(addOrderDTO);
        return "admin/admin";
    }

    @GetMapping("/edit-order/{id}")
    public String editOrder(@PathVariable("id") Long id, Model model) {

        AddOrderDTO addOrderDTO = adminService.getOrderById(id);


        if (addOrderDTO == null)
            return "error";

        model.addAttribute("addOrderDTO", addOrderDTO);
        model.addAttribute("productId", id);
        model.addAttribute("orderStatusOptions", OrderStatusEnum.values());

        return "admin/order/edit-order";
    }

    @PostMapping("/edit-order/{id}")
    public String editOrder(@PathVariable("id") Long id, AddOrderDTO addOrderDTO) {

        adminService.editOrder(id, addOrderDTO);
        return "admin/admin";
    }

    @GetMapping("/delete-order/{id}")
    public String deleteOrderGet(@PathVariable("id") Long id) {

        adminService.deleteOrder(id);

        return "admin/admin";
    }

    @PostMapping("/delete-order/{id}")
    public String deleteOrderPost(@PathVariable("id") Long id) {

        adminService.deleteOrder(id);

        return "admin/admin";
    }

    //endregion

    //region <Discount CRUD>
    @GetMapping("/add-discount")
    public String addDiscount(Model model) {

        model.addAttribute("addDiscountDTO", new AddDiscountDTO());

        return "admin/discount/add-discount";
    }

    @PostMapping("/add-discount")
    public String addDiscount(AddDiscountDTO addDiscountDTO) {
        adminService.addDiscount(addDiscountDTO);
        return "admin/admin";
    }

    @GetMapping("/edit-discount/{id}")
    public String editDiscount(@PathVariable("id") Long id, Model model) {

        AddDiscountDTO addDiscountDTO = adminService.getDiscountById(id);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (addDiscountDTO == null)
            return "error";

        model.addAttribute("addDiscountDTO", addDiscountDTO);
        model.addAttribute("startDate",format.format(addDiscountDTO.getStartDate()));
        model.addAttribute("endDate",format.format(addDiscountDTO.getEndDate()));
        model.addAttribute("discountId", id);

        return "admin/discount/edit-discount";
    }

    @PostMapping("/edit-discount/{id}")
    public String editDiscount(@PathVariable("id") Long id, AddDiscountDTO addDiscountDTO) {
        adminService.editDiscount(id, addDiscountDTO);
        return "admin/admin";
    }

    @GetMapping("/delete-discount/{id}")
    public String deleteDiscountGet(@PathVariable("id") Long id) {

        adminService.deleteDiscount(id);

        return "admin/admin";
    }

    @PostMapping("/delete-discount/{id}")
    public String deleteDiscountPost(@PathVariable("id") Long id) {

        adminService.deleteDiscount(id);

        return "admin/admin";
    }
    //endregion
}
