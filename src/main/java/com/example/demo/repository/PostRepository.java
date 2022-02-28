package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	public Post findOneBySlug(String slug);

	@Query("select entity from Post entity where entity.display = 1")
	public Page<Post> getListPagination(Pageable pageable);

	@Query("select entity from Post entity where entity.display = 1")
	public List<Post> getList();
	
//	@Query("select t from Test t join t.users u where u.username = :username")
	Page<Post> findByTags_slug(String slug, Pageable pageable);

}
