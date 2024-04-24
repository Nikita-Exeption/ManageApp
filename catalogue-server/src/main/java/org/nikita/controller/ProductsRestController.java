package org.nikita.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nikita.controller.payload.NewProductPayLoad;
import org.nikita.entity.Product;
import org.nikita.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsRestController {

    private final ProductService service;

    @GetMapping
    public List<Product> findProducts() {
        return service.findAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayLoad payLoad,
                                           BindingResult result,
                                           UriComponentsBuilder builder)
            throws BindException {
        if (result.hasErrors()) {
            if (result instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(result);
            }
        } else {
            Product product = service.createNewProduct(payLoad.title(), payLoad.details());
            return ResponseEntity.created(builder
                            .replacePath("catalogue-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }
    }

}

