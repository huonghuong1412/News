package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

	public Boolean existsByUrl(String url);

	public News getOneByUrl(String url);
	
	public News findOneBySlug(String slug);

	@Query("select entity from News entity where entity.display = 1 and entity.category.slug=?1")
	public List<News> getList(String category, Pageable pageable);

	Page<News> findByTags_slug(String slug, Pageable pageable);

}
