package com.denisitch.catalogue.repository;

import com.denisitch.catalogue.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
