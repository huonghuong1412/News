package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Tag;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Long> {

	public Tag findOneBySlug(String slug);
	
	public Tag findOneByName(String name);
	
	@Query("select entity from Tag entity")
	public Page<Tag> getList(Pageable pageable);
	
}
