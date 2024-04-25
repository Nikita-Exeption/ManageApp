package org.nikita.service;

import lombok.RequiredArgsConstructor;
import org.nikita.entity.Product;
import org.nikita.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductServiceImpl implements ProductService {

    private final ProductRepository repository;


    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if (filter != null && !filter.isBlank()) {
            return repository.findAllByTitleLikeIgnoreCase(filter);
        } else {
            return repository.findAll();
        }
    }

    @Override
    @Transactional
    public Product createNewProduct(String title, String details) {
        return repository.save(new Product(null, title, details));
    }

    @Override
    public Optional<Product> findProductById(Integer id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String details) {
        repository.findById(id).ifPresentOrElse(x -> {
                    x.setTitle(title);
                    x.setDetails(details);

                    repository.save(x);
                }, () -> {
                    throw new NoSuchElementException();
                }
        );
    }

    @Override
    @Transactional
    public void deleteProductById(Integer id) {
        repository.deleteById(id);
    }


}
