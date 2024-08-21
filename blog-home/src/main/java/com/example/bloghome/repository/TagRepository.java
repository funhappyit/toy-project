package com.example.bloghome.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloghome.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
