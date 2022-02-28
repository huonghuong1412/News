package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query("select entity from User entity where entity.email = ?1")
	Optional<User> findByEmail(String email);

	public boolean existsByEmail(String email);

	public User findOneByEmail(String email);
}
