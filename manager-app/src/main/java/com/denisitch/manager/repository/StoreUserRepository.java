package com.denisitch.manager.repository;

import com.denisitch.manager.entity.StoreUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StoreUserRepository extends CrudRepository<StoreUser, Integer> {

    Optional<StoreUser> findByUsername(String username);
}
