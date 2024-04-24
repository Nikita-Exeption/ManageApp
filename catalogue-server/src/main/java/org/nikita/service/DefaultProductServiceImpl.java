package org.nikita.service;

import lombok.RequiredArgsConstructor;
import org.nikita.entity.Product;
import org.nikita.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductServiceImpl implements ProductService {

    private final ProductRepository repository;


    @Override
    public List<Product> findAllProducts() {
        return repository.getAll();
    }

    @Override
    public Product createNewProduct(String title, String details) {
        return repository.addNewProduct(title, details);
    }

    @Override
    public Optional<Product> findProductById(Integer id) {
        return repository.getById(id);
    }

    @Override
    public void updateProduct(Integer id, String title, String details) {
        repository.getById(id).ifPresentOrElse(x -> {
                    x.setTitle(title);
                    x.setDetails(details);
                }, () -> { throw new NoSuchElementException();}
        );
    }

    @Override
    public void deleteProductById(Integer id) {
        repository.deleteById(id);
    }


}
