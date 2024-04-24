package org.nikita.repository;

import org.nikita.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.IntStream;

@Repository
public class InMemoryProductRepositoryImpl implements ProductRepository {

    private final List<Product> productList = Collections.synchronizedList(new LinkedList<>());

    public InMemoryProductRepositoryImpl() {
        IntStream.range(1, 4).
                forEach(i -> productList.add(new Product(i, "title %d".formatted(i), "description")));
    }

    @Override
    public List<Product> getAll() {
        return Collections.unmodifiableList(productList);
    }

    @Override
    public Product addNewProduct(String title, String details) {
        Product product = new Product(productList.size() + 1, title, details);
        productList.add(product);
        return product;
    }

    @Override
    public Optional<Product> getById(Integer id) {
        return productList.stream().filter(x -> Objects.equals(x.getId(), id)).findFirst();
    }

    @Override
    public void deleteById(Integer id) {
        productList.removeIf(x -> x.getId().equals(id));
    }


}
