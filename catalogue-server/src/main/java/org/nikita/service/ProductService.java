package org.nikita.service;

import org.nikita.entity.Product;

import java.util.Optional;

public interface ProductService {
    Iterable<Product> findAllProducts(String filter);

    Product createNewProduct(String title, String details);

    Optional<Product> findProductById(Integer id);

    void updateProduct(Integer id, String title, String details);

    void deleteProductById(Integer id);

}
