package org.nikita.repository;

import org.nikita.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> getAll();

    Product addNewProduct(String title, String details);

    Optional<Product> getById(Integer id);

    void deleteById(Integer id);
}
