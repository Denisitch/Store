package com.denisitch.catalogue.repository;

import com.denisitch.catalogue.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    Iterable<Product> findByTitleLikeIgnoreCase(String title);
}
