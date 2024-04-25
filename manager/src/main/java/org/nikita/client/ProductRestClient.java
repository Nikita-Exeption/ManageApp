package org.nikita.client;

import org.nikita.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRestClient {

    List<Product> findAllProducts(String filter);

    Product createProduct(String title, String details);

    Optional<Product> findProductById(Integer id);

    void updateProduct(int id, String title, String details);

    void deleteProduct(int id);
}
