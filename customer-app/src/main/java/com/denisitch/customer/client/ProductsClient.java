package com.denisitch.customer.client;

import com.denisitch.customer.entity.Product;
import reactor.core.publisher.Flux;

public interface ProductsClient {

    Flux<Product> findAllProducts(String filter);
}
