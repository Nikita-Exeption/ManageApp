package org.nikita.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nikita.controller.payload.NewProductPayLoad;
import org.nikita.entity.Product;
import org.nikita.service.ProductService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {


    private final ProductService service;

    @GetMapping("/list")
    public String getProducts(Model model) {
        model.addAttribute("products", service.findAllProducts());
        return "catalogue/products/list";
    }

    @GetMapping("/create")
    public String getNewProduct() {
        return "catalogue/products/create";
    }

    @PostMapping("create")
    public String createProduct(@Valid NewProductPayLoad payLoad, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("payLoad", payLoad);
            model.addAttribute("errors", result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "catalogue/products/create";
        } else {
            Product product = service.createNewProduct(payLoad.title(), payLoad.details());
            return "redirect:/catalogue/products/%d".formatted(product.getId());
        }
    }

}
