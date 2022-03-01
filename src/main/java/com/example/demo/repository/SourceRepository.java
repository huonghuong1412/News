package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Source;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {

	public Boolean existsBySlug(String slug);
	
}
