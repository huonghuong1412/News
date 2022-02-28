package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PostDto;
import com.example.demo.dto.ResponseMessage;
import com.example.demo.model.Author;
import com.example.demo.model.NewsCategory;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.NewsCategoryRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.TagsRepository;
import com.example.demo.utils.Slug;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/post")
public class PostController {

	@Autowired
	private EntityManager manager;

	@Autowired
	private PostRepository postRepos;

	@Autowired
	private NewsCategoryRepository newsCategoryRepository;

	@Autowired
	private AuthorRepository authorRepos;

	@Autowired
	private TagsRepository tagRepos;

	@GetMapping(value = "")
	public ResponseEntity<Page<PostDto>> getAllNews(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
		Integer pageIndex = page > 0 ? page -= 1 : 0;
		String whereClause = "";
		String orderBy = " ORDER BY createdDate DESC";
		String sqlCount = "select count(entity.id) from  Post as entity where entity.display=1 AND (1=1) ";
		String sql = "select new com.example.demo.dto.PostDto(entity) from Post as entity where entity.display=1 AND (1=1) ";

		sql += whereClause + orderBy;
		sqlCount += whereClause;
		Query q = manager.createQuery(sql, PostDto.class);
		Query qCount = manager.createQuery(sqlCount);

		int startPosition = pageIndex * limit;
		q.setFirstResult(startPosition);
		q.setMaxResults(limit);

		@SuppressWarnings("unchecked")
		List<PostDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();
		Pageable pageable = PageRequest.of(pageIndex, limit);
		Page<PostDto> result = new PageImpl<PostDto>(entities, pageable, count);

		return new ResponseEntity<Page<PostDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/search")
	public ResponseEntity<Page<PostDto>> searchNews(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		Integer pageIndex = page > 0 ? page -= 1 : 0;
		String whereClause = "";
		String orderBy = " ORDER BY createdDate DESC";
		String sqlCount = "select count(entity.id) from Post as entity where (1=1) ";
		String sql = "select new com.example.demo.dto.PostDto(entity) from Post as entity where entity.display=1 AND (1=1) ";
		if (keyword != null && StringUtils.hasText(keyword)) {

			if (keyword.contains(" ")) {
				String[] keywords = keyword.split(" ");
				whereClause += " AND ( entity.title LIKE " + "'" + keywords[0] + "'" + " OR entity.slug LIKE " + "'"
						+ keywords[0] + "'" + " OR entity.content LIKE " + "'" + keywords[0] + "'";
				for (int i = 1; i < keywords.length; i++) {
					whereClause += " or entity.title LIKE " + "'" + keywords[i] + "'" + " OR entity.slug LIKE " + "'"
							+ keywords[i] + "'" + " OR entity.content LIKE " + "'" + keywords[i] + "'";
				}
				whereClause += " ) ";
			} else {
				whereClause += " AND ( entity.title LIKE :text " + "OR entity.slug LIKE :text "
						+ "OR entity.content LIKE :text )";
			}
		}

		sql += whereClause + orderBy;
		sqlCount += whereClause;

		Query q = manager.createQuery(sql, PostDto.class);
		Query qCount = manager.createQuery(sqlCount);

		if (keyword != null && StringUtils.hasText(keyword)) {
			if (keyword.contains(" ")) {

			} else {
				q.setParameter("text", '%' + keyword + '%');
				qCount.setParameter("text", '%' + keyword + '%');
			}

		}
		int startPosition = pageIndex * limit;
		q.setFirstResult(startPosition);
		q.setMaxResults(limit);

		@SuppressWarnings("unchecked")
		List<PostDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();
		Pageable pageable = PageRequest.of(pageIndex, limit);
		Page<PostDto> result = new PageImpl<PostDto>(entities, pageable, count);
		return new ResponseEntity<Page<PostDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/list/{category}")
	public ResponseEntity<Page<PostDto>> getAllByCategory(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
			@RequestParam(name = "keyword", defaultValue = "") String keyword, @PathVariable String category) {
		Integer pageIndex = page > 0 ? page -= 1 : 0;
		String whereClause = "";
		String orderBy = " ORDER BY createdDate DESC";
		String sqlCount = "select count(entity.id) from Post as entity where entity.display=1 AND (1=1) ";
		String sql = "select new com.example.demo.dto.PostDto(entity) from Post as entity where entity.display=1 AND (1=1) ";
		if (keyword != null && StringUtils.hasText(keyword)) {
			if (keyword.contains(" ")) {
				String[] keywords = keyword.split(" ");
				whereClause += " AND ( entity.title LIKE " + "'" + keywords[0] + "'" + " OR entity.slug LIKE " + "'"
						+ keywords[0] + "'" + " OR entity.content LIKE " + "'" + keywords[0] + "'";
				for (int i = 1; i < keywords.length; i++) {
					whereClause += " or entity.title LIKE " + "'" + keywords[i] + "'" + keywords[i] + "'"
							+ " OR entity.slug LIKE " + "'" + keywords[i] + "'" + " OR entity.content LIKE " + "'"
							+ keywords[i] + "'";
				}
				whereClause += " ) ";
			} else {
				whereClause += " AND ( entity.title LIKE :text " + "OR entity.slug LIKE :text "
						+ "OR entity.content LIKE :text )";
			}
		}
		if (category != null && StringUtils.hasText(category)) {
			whereClause += " AND ( entity.category.slug LIKE :category )";
		}
		sql += whereClause + orderBy;
		sqlCount += whereClause;
		Query q = manager.createQuery(sql, PostDto.class);
		Query qCount = manager.createQuery(sqlCount);

		if (keyword != null && StringUtils.hasText(keyword)) {
			if (keyword.contains(" ")) {

			} else {
				q.setParameter("text", '%' + keyword + '%');
				qCount.setParameter("text", '%' + keyword + '%');
			}
		}
		if (category != null && StringUtils.hasText(category)) {
			q.setParameter("category", category);
			qCount.setParameter("category", category);
		}
		int startPosition = pageIndex * limit;
		q.setFirstResult(startPosition);
		q.setMaxResults(limit);

		@SuppressWarnings("unchecked")
		List<PostDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();
		Pageable pageable = PageRequest.of(pageIndex, limit);
		Page<PostDto> result = new PageImpl<PostDto>(entities, pageable, count);
		return new ResponseEntity<Page<PostDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/tag/{tag}")
	public ResponseEntity<Page<PostDto>> getPostByTag(@PathVariable String tag,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
		Page<Post> list = postRepos.findByTags_slug(tag, PageRequest.of(page, limit, Sort.by(sortBy).descending()));
		Page<PostDto> result = list.map(item -> new PostDto(item));
		return new ResponseEntity<Page<PostDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/{slug}")
	public ResponseEntity<PostDto> getPostBySlug(@PathVariable String slug) {
		Post entity = postRepos.findOneBySlug(slug);
		PostDto result = new PostDto(entity);
		return new ResponseEntity<PostDto>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/id/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
		Post entity = postRepos.getById(id);
		PostDto result = new PostDto(entity);
		return new ResponseEntity<PostDto>(result, HttpStatus.OK);
	}

	@PostMapping(value = "/add")
	public ResponseEntity<ResponseMessage> create(@RequestBody PostDto dto) {
		if (dto != null) {
			Post entity = null;
			Tag tag = null;
			List<String> tagNames = dto.getTag_names();
			List<Tag> tags = new ArrayList<>();
			NewsCategory category = newsCategoryRepository.findOneBySlug(dto.getCategory_slug());
			Author author = authorRepos.findOneByName(dto.getAuthor_name());
			if (dto.getId() != null) {
				entity = postRepos.getById(dto.getId());
				entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (entity == null) {
				entity = new Post();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
			}

			if (tagNames != null) {
				for (String item : tagNames) {
					tag = tagRepos.findOneByName(item);
					if (tag != null) {
						tags.add(tag);
					} else {
						tag = new Tag();
						tag.setName(item);
						tag.setSlug(Slug.makeCode(item));
						tag.setCreatedDate(new Timestamp(new Date().getTime()).toString());
						tagRepos.save(tag);
						tags.add(tag);
					}
				}
			}

			entity.setTitle(dto.getTitle());
			entity.setSlug(Slug.makeSlug(dto.getTitle()));
			entity.setImage(dto.getImage());
			entity.setContent(dto.getContent());
			entity.setCategory(category);
			entity.setAuthor(author);
			entity.setTags(tags);
			entity.setDisplay(1);
			entity = postRepos.save(entity);
			if (entity != null) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Thêm bài viết thành công!"),
						HttpStatus.OK);
			}
		}
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Thêm bài viết không thành công!"),
				HttpStatus.BAD_REQUEST);

	}

	@PutMapping(value = "/update/{id}")
	public ResponseEntity<ResponseMessage> update(@RequestBody PostDto dto, @PathVariable Long id) {
		dto.setId(id);
		if (dto != null) {
			Post entity = null;
			Tag tag = null;
			List<String> tagNames = dto.getTag_names();
			List<Tag> tags = new ArrayList<>();
			NewsCategory category = newsCategoryRepository.findOneBySlug(dto.getCategory_slug());
			Author author = authorRepos.findOneByName(dto.getAuthor_name());
			if (dto.getId() != null) {
				entity = postRepos.getById(dto.getId());
				entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (entity == null) {
				entity = new Post();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (tagNames != null) {
				for (String item : tagNames) {
					tag = tagRepos.findOneByName(item);
					if (tag != null) {
						tags.add(tag);
					} else {
						tag = new Tag();
						tag.setName(item);
						tag.setSlug(Slug.makeCode(item));
						tag.setCreatedDate(new Timestamp(new Date().getTime()).toString());
						tagRepos.save(tag);
						tags.add(tag);
					}
				}
			}
			entity.setTitle(dto.getTitle());
			entity.setSlug(Slug.makeSlug(dto.getTitle()));
			entity.setContent(dto.getContent());
			entity.setCategory(category);
			entity.setAuthor(author);
			entity.setDisplay(1);
			entity.setTags(tags);
			entity = postRepos.save(entity);
			if (entity != null) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Sửa bài viết thành công!"),
						HttpStatus.OK);
			}
		}
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Sửa bài viết không thành công!"),
				HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ResponseMessage> delete(@PathVariable Long id) {
		if (id != null) {
			Post entity = postRepos.getById(id);
			if (entity.getDisplay() == 1) {
				entity.setDisplay(0);
			} else {
				entity.setDisplay(1);
			}
			entity = postRepos.save(entity);
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Ẩn bài viết thành công!"),
					HttpStatus.OK);
		}
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Ẩn bài viết không thành công!"),
				HttpStatus.BAD_REQUEST);
	}
}
