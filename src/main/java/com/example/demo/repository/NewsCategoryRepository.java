package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.NewsCategory;

@Repository
public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {

	public NewsCategory findOneBySlug(String slug);
	
}
