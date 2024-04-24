package org.nikita.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.nikita.client.BadRequestException;
import org.nikita.client.ProductRestClient;
import org.nikita.controller.payload.UpdateProductPayLoad;
import org.nikita.entity.Product;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(("catalogue/products/{productId:\\d+}"))
@RequiredArgsConstructor
public class ProductController {

    private final ProductRestClient service;

    private final MessageSource source;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") Integer productId) {
        return service.findProductById(productId).orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public String getProductById() {
        return "catalogue/products/product";
    }

    @GetMapping("/edit")
    public String editProductByID() {
        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute Product product, UpdateProductPayLoad payLoad, Model model) {
        try {
            this.service.updateProduct(product.id(), payLoad.title(), payLoad.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payLoad);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/edit";
        }
    }

    @PostMapping("delete")
    public String deleteById(@ModelAttribute Product product) {
        service.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handlerNoSuchElementException(NoSuchElementException exception, Model model,
                                                HttpServletResponse response, Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", source.getMessage(exception.getMessage(), new Object[0], exception.getMessage(), locale));
        return "catalogue/products/error";
    }
}
