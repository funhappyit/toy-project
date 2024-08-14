package com.example.jpablog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpablog.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
	User findByUsername(String username);
}
