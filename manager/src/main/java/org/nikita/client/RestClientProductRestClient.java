package org.nikita.client;

import lombok.RequiredArgsConstructor;
import org.nikita.controller.payload.NewProductPayLoad;
import org.nikita.entity.Product;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;


@RequiredArgsConstructor
public class RestClientProductRestClient implements ProductRestClient {

    private static final ParameterizedTypeReference<List<Product>> PRODUCTS_PARAMETERIZED_TYPE = new ParameterizedTypeReference<>() {

    };

    private final RestClient client;

    @Override
    public List<Product> findAllProducts() {
        return client
                .get()
                .uri("catalogue-api/products")
                .retrieve()
                .body(PRODUCTS_PARAMETERIZED_TYPE);
    }

    @Override
    public Product createProduct(String title, String details) {
        try {
            return client
                    .post()
                    .uri("catalogue-api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewProductPayLoad(title, details))
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Product> findProductById(Integer id) {
        try {
            return Optional.ofNullable(client
                    .get()
                    .uri("catalogue-api/products/{productId}", id)
                    .retrieve()
                    .body(Product.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(int id, String title, String details) {
        try {
            client
                    .patch()
                    .uri("catalogue-api/products/{productId}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewProductPayLoad(title, details))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>)Objects.requireNonNull(problemDetail.getProperties()).get("errors"));
        }

    }

    @Override
    public void deleteProduct(int id) {
        try {
            client
                    .delete()
                    .uri("catalogue-api/products/{productId}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException exception) {
            throw new NoSuchElementException(exception);
        }
    }
}
