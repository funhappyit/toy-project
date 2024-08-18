package com.example.bloghome.repository;

import com.example.bloghome.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {
	User findByUsername(String username);
}
