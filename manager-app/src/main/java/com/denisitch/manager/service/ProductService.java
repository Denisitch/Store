package com.denisitch.manager.service;

import com.denisitch.manager.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAllProducts();

    Product createProduct(String title, String details);
}
