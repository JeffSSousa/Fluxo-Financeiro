package com.jeffssousa.fluxo.repository;

import com.jeffssousa.fluxo.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);

    Optional<Category> findByNameAndUserUserId(String name, UUID userId);
}
