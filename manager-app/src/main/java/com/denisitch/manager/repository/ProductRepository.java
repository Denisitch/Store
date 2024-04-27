package com.denisitch.manager.repository;

import com.denisitch.manager.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();

    Product save(Product product);
}
