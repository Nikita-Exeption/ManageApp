package org.nikita.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nikita.controller.payload.UpdateProductPayLoad;
import org.nikita.entity.Product;
import org.nikita.service.ProductService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products/{productId:\\d+}")
public class ProductRestController {

    private final ProductService service;

    private final MessageSource source;

    @ModelAttribute
    public Product product(@PathVariable("productId") Integer id) {
        return service.findProductById(id).orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public Product findProduct(@ModelAttribute Product product) {
        return product;
    }

    @PatchMapping
    public ResponseEntity<?> updateProduct(@PathVariable Integer productId,
                                           @RequestBody @Valid UpdateProductPayLoad payLoad,
                                           BindingResult result)
            throws BindException {
        if (result.hasErrors()) {
            if (result instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(result);
            }
        } else {
            service.updateProduct(productId, payLoad.title(), payLoad.details());
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        service.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception, Locale locale){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                    source.getMessage(exception.getMessage(), new Object[0], exception.getMessage(), locale)));

    }
}
