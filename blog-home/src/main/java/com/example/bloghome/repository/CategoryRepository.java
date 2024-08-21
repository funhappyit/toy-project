package com.example.bloghome.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloghome.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
