package org.nikita.controller;

import lombok.RequiredArgsConstructor;
import org.nikita.client.BadRequestException;
import org.nikita.client.ProductRestClient;
import org.nikita.controller.payload.NewProductPayLoad;
import org.nikita.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {


    private final ProductRestClient service;

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
    public String createProduct(NewProductPayLoad payLoad, Model model) {
        try {
            Product product = service.createProduct(payLoad.title(), payLoad.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("errors", exception.getErrors());
            model.addAttribute("payLoad", payLoad);
            return "redirect:/catalogue/create";
        }
    }

}
