package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

	public Boolean existsByUrl(String url);
	
	public News getOneByUrl(String url);
	
}
