package org.nikita.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.nikita.controller.payload.UpdateProductPayLoad;
import org.nikita.entity.Product;
import org.nikita.service.ProductService;
import org.springframework.cglib.core.Local;
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

    private final ProductService service;

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
    public String updateProduct(@ModelAttribute Product product, UpdateProductPayLoad payLoad) {
        service.updateProduct(product.getId(), payLoad.title(), payLoad.details());
        return "redirect:/catalogue/products/%d".formatted(product.getId());
    }

    @PostMapping("delete")
    public String deleteById(@ModelAttribute Product product){
        service.deleteProductById(product.getId());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handlerNoSuchElementException(NoSuchElementException exception, Model model,
                                                HttpServletResponse response, Locale locale){
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", source.getMessage(exception.getMessage(), new Object[0], exception.getMessage(), locale));
        return "catalogue/products/error";
    }
}
