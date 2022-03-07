package com.example.demo.controller;

import java.io.IOException;
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

import com.example.demo.dto.NewsCrawlDetail;
import com.example.demo.dto.NewsDto;
import com.example.demo.dto.ResponseMessage;
import com.example.demo.dto.SearchDto;
import com.example.demo.model.Author;
import com.example.demo.model.News;
import com.example.demo.model.NewsCategory;
import com.example.demo.model.Source;
import com.example.demo.model.Tag;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.NewsCategoryRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.NewsSourceRepository;
import com.example.demo.repository.TagsRepository;
import com.example.demo.service.CrawlService;
import com.example.demo.utils.Slug;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/news/article")
public class NewsController {

	@Autowired
	private EntityManager manager;

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private NewsCategoryRepository newsCategoryRepository;

	@Autowired
	private NewsSourceRepository sourceRepository;

	@Autowired
	private AuthorRepository authorRepos;

	@Autowired
	private TagsRepository tagRepos;

	@Autowired
	private CrawlService crawlService;

	@GetMapping(value = "")
	public ResponseEntity<Page<NewsDto>> getAllNews(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
		Page<News> list = newsRepository.findAll(PageRequest.of(page, limit, Sort.by(sortBy).descending()));
		Page<NewsDto> result = list.map(tag -> new NewsDto(tag));
		return new ResponseEntity<Page<NewsDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/all")
	public ResponseEntity<Page<NewsDto>> searchNews(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@RequestParam(name = "category", defaultValue = "") String category,
			@RequestParam(name = "source", defaultValue = "") String source,
			@RequestParam(name = "display", defaultValue = "2") Integer display) {

		SearchDto dto = new SearchDto();
		dto.setDisplay(display);
		dto.setKeyword(keyword);
		dto.setSource(source);
		dto.setCategory(category);

		Integer pageIndex = page > 0 ? page -= 1 : 0;
		String whereClause = "";
		String orderBy = " ORDER BY createdDate DESC";
		String sqlCount = "select count(entity.id) from  News as entity where (1=1) ";
		String sql = "select new com.example.demo.dto.NewsDto(entity) from News as entity where (1=1) ";
		if (dto.getDisplay() == 0 || dto.getDisplay() == 1) {
			whereClause += " AND ( entity.display = " + dto.getDisplay() + ")";
		} else {
			whereClause += "";
		}
		if (keyword != null && StringUtils.hasText(keyword)) {
			if (keyword.contains(" ")) {
				String[] keywords = keyword.split(" ");
				whereClause += " AND ( entity.title LIKE " + "'" + keywords[0] + "'" + " OR entity.slug LIKE " + "'"
						+ keywords[0] + "'" + " OR entity.content LIKE " + "'" + keywords[0] + "'"
						+ " OR entity.short_description LIKE " + "'" + keywords[0] + "'";
				for (int i = 1; i < keywords.length; i++) {
					whereClause += " or entity.title LIKE " + "'" + keywords[i] + "'"
							+ " OR entity.short_description LIKE " + "'" + keywords[i] + "'" + " OR entity.slug LIKE "
							+ "'" + keywords[i] + "'" + " OR entity.content LIKE " + "'" + keywords[i] + "'";
				}
				whereClause += " ) ";
			} else {
				whereClause += " AND ( entity.title LIKE :text " + "OR entity.slug LIKE :text "
						+ "OR entity.short_description LIKE :text " + "OR entity.content LIKE :text )";
			}
		}

		if (dto.getCategory() != null && StringUtils.hasText(dto.getCategory())) {
			whereClause += " AND ( entity.category.slug = :category )";
		} else {
			whereClause += "";
		}

		if (dto.getSource() != null && StringUtils.hasText(dto.getSource())) {
			whereClause += " AND ( entity.source.slug = :source )";
		} else {
			whereClause += "";
		}

		sql += whereClause + orderBy;
		sqlCount += whereClause;

		System.out.println(sql);

		Query q = manager.createQuery(sql, NewsDto.class);
		Query qCount = manager.createQuery(sqlCount);

		if (keyword != null && StringUtils.hasText(keyword)) {
			if (keyword.contains(" ")) {

			} else {
				q.setParameter("text", '%' + keyword + '%');
				qCount.setParameter("text", '%' + keyword + '%');
			}

		}
		if (dto.getCategory() != null && dto.getCategory().length() > 0) {
			q.setParameter("category", dto.getCategory());
			qCount.setParameter("category", dto.getCategory());
		}

		if (dto.getSource() != null && dto.getSource().length() > 0) {
			q.setParameter("source", dto.getSource());
			qCount.setParameter("source", dto.getSource());
		}

		int startPosition = pageIndex * limit;
		q.setFirstResult(startPosition);
		q.setMaxResults(limit);

		@SuppressWarnings("unchecked")
		List<NewsDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();
		Pageable pageable = PageRequest.of(pageIndex, limit);
		Page<NewsDto> result = new PageImpl<NewsDto>(entities, pageable, count);
		return new ResponseEntity<Page<NewsDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/search")
	public ResponseEntity<Page<NewsDto>> searchNews(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		Integer pageIndex = page > 0 ? page -= 1 : 0;
		String whereClause = "";
		String orderBy = " ORDER BY createdDate DESC";
		String sqlCount = "select count(entity.id) from  News as entity where (1=1) ";
		String sql = "select new com.example.demo.dto.NewsDto(entity) from News as entity where entity.display=1 AND (1=1) ";
		if (keyword != null && StringUtils.hasText(keyword)) {

			if (keyword.contains(" ")) {
				String[] keywords = keyword.split(" ");
				whereClause += " AND ( entity.title LIKE " + "'" + keywords[0] + "'" + " OR entity.slug LIKE " + "'"
						+ keywords[0] + "'" + " OR entity.content LIKE " + "'" + keywords[0] + "'"
						+ " OR entity.short_description LIKE " + "'" + keywords[0] + "'";
				for (int i = 1; i < keywords.length; i++) {
					whereClause += " or entity.title LIKE " + "'" + keywords[i] + "'"
							+ " OR entity.short_description LIKE " + "'" + keywords[i] + "'" + " OR entity.slug LIKE "
							+ "'" + keywords[i] + "'" + " OR entity.content LIKE " + "'" + keywords[i] + "'";
				}
				whereClause += " ) ";
			} else {
				whereClause += " AND ( entity.title LIKE :text " + "OR entity.slug LIKE :text "
						+ "OR entity.short_description LIKE :text " + "OR entity.content LIKE :text )";
			}

		}

		sql += whereClause + orderBy;
		sqlCount += whereClause;

		Query q = manager.createQuery(sql, NewsDto.class);
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
		List<NewsDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();
		Pageable pageable = PageRequest.of(pageIndex, limit);
		Page<NewsDto> result = new PageImpl<NewsDto>(entities, pageable, count);
		return new ResponseEntity<Page<NewsDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/{category}")
	public ResponseEntity<Page<NewsDto>> getAllByCategory(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
			@RequestParam(name = "keyword", defaultValue = "") String keyword, @PathVariable String category) {
		Integer pageIndex = page > 0 ? page -= 1 : 0;
		String whereClause = "";
		String orderBy = " ORDER BY createdDate DESC";
		String sqlCount = "select count(entity.id) from  News as entity where entity.display=1 AND (1=1) ";
		String sql = "select new com.example.demo.dto.NewsDto(entity) from News as entity where entity.display=1 AND (1=1) ";
		if (keyword != null && StringUtils.hasText(keyword)) {
			if (keyword.contains(" ")) {
				String[] keywords = keyword.split(" ");
				whereClause += " AND ( entity.title LIKE " + "'" + keywords[0] + "'" + " OR entity.slug LIKE " + "'"
						+ keywords[0] + "'" + " OR entity.content LIKE " + "'" + keywords[0] + "'"
						+ " OR entity.short_description LIKE " + "'" + keywords[0] + "'";
				for (int i = 1; i < keywords.length; i++) {
					whereClause += " or entity.title LIKE " + "'" + keywords[i] + "'"
							+ " OR entity.short_description LIKE " + "'" + keywords[i] + "'" + " OR entity.slug LIKE "
							+ "'" + keywords[i] + "'" + " OR entity.content LIKE " + "'" + keywords[i] + "'";
				}
				whereClause += " ) ";
			} else {
				whereClause += " AND ( entity.title LIKE :text " + "OR entity.slug LIKE :text "
						+ "OR entity.short_description LIKE :text " + "OR entity.content LIKE :text )";
			}
		}
		if (category != null && StringUtils.hasText(category)) {
			whereClause += " AND ( entity.category.slug LIKE :category )";
		}
		sql += whereClause + orderBy;
		sqlCount += whereClause;
		Query q = manager.createQuery(sql, NewsDto.class);
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
		List<NewsDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();
		Pageable pageable = PageRequest.of(pageIndex, limit);
		Page<NewsDto> result = new PageImpl<NewsDto>(entities, pageable, count);
		return new ResponseEntity<Page<NewsDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/tag/{tag}")
	public ResponseEntity<Page<NewsDto>> getPostByTag(@PathVariable String tag,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
		Page<News> list = newsRepository.findByTags_slug(tag,
				PageRequest.of(page, limit, Sort.by(sortBy).descending()));
		Page<NewsDto> result = list.map(item -> new NewsDto(item));
		return new ResponseEntity<Page<NewsDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/slug/{slug}")
	public ResponseEntity<NewsDto> getPostBySlug(@PathVariable String slug) {
		News entity = newsRepository.findOneBySlug(slug);
		NewsDto result = new NewsDto(entity);
		return new ResponseEntity<NewsDto>(result, HttpStatus.OK);
	}

	@PostMapping(value = "/add")
	public ResponseEntity<ResponseMessage> add(@RequestBody List<NewsDto> dtos) throws IOException {
		List<News> entities = new ArrayList<>();
		if (dtos != null && dtos.size() > 0) {
			for (NewsDto dto : dtos) {
				News entity = null;
				Tag tag = null;
				NewsCategory category = newsCategoryRepository.findOneBySlug(dto.getCategory_slug());
				Source source = sourceRepository.findOneBySlug(dto.getSource_slug());
				Author author = authorRepos.findOneByName(dto.getAuthor_name());
				List<String> tagNames = dto.getTag_names();
				List<Tag> tags = new ArrayList<>();

				if (newsRepository.existsByUrl(dto.getUrl())) {
					entity = newsRepository.getOneByUrl(dto.getUrl());
					return new ResponseEntity<ResponseMessage>(
							new ResponseMessage("SUCCESS", "Bài viết " + entity.getTitle() + " đã tồn tại!"),
							HttpStatus.OK);
				} else {
					entity = new News();
					entity.setTitle(dto.getTitle());
					entity.setSlug(Slug.makeSlug(dto.getTitle()));
					entity.setShort_description(dto.getShort_description());
					entity.setImage(dto.getImage());
					entity.setContent(dto.getContent());
					entity.setUrl(dto.getUrl());
					entity.setCategory(category);
					entity.setSource(source);
					if (author == null) {
						author = new Author();
						author.setName(dto.getAuthor_name());
						author.setSlug(Slug.makeCode(dto.getAuthor_name()));
						author.setDisplay(1);
						author.setCreatedDate(new Timestamp(new Date().getTime()).toString());
						authorRepos.save(author);
					}
					entity.setAuthor(author);

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
					entity.setTags(tags);
					entity.setDisplay(1);
					entities.add(entity);
				}
			}
			newsRepository.saveAll(entities);
			if (entities != null && entities.size() > 0) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Thêm bài viết thành công!"),
						HttpStatus.OK);
			}
		}
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Thêm bài viết không thành công!"),
				HttpStatus.BAD_REQUEST);

	}

	@GetMapping(value = "/test") 
	public ResponseEntity<Object> test() throws IOException {
		NewsCrawlDetail detail = crawlService.getData("vnexpress", "https://vnexpress.net/viet-nam-de-nghi-trung-quoc-khong-vi-pham-vung-dac-quyen-kinh-te-4435157.html");
		return new ResponseEntity<Object>(detail, HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/create")
	public ResponseEntity<ResponseMessage> create(@RequestBody List<NewsDto> dtos) throws IOException {
		List<News> entities = new ArrayList<>();
		if (dtos != null) {
			for (NewsDto dto : dtos) {
				News entity = null;
				Tag tag = null;
				NewsCategory category = newsCategoryRepository.findOneBySlug(dto.getCategory_slug());
				Source source = sourceRepository.findOneBySlug(dto.getSource_slug());

				Author authorEntity = null;
				List<Tag> tags = new ArrayList<>();

				NewsCrawlDetail detail = crawlService.getData(dto.getSource_slug(), dto.getUrl());

				if (newsRepository.existsByUrl(dto.getUrl())) {
					entity = newsRepository.getOneByUrl(dto.getUrl());
					return new ResponseEntity<ResponseMessage>(
							new ResponseMessage("SUCCESS", "Bài viết " + entity.getTitle() + " đã có!"), HttpStatus.OK);
				} else {
					entity = new News();
					entity.setTitle(dto.getTitle());
					entity.setSlug(Slug.makeSlug(dto.getTitle()));
					entity.setShort_description(dto.getShort_description());
					entity.setImage(dto.getImage());
					entity.setContent(detail.getContent());
					entity.setUrl(dto.getUrl());
					entity.setCategory(category);
					entity.setSource(source);

					authorEntity = authorRepos.findOneByName(detail.getAuthor());

					if (authorEntity == null) {
						authorEntity = new Author();
						authorEntity.setName(detail.getAuthor());
						authorEntity.setSlug(Slug.makeCode(detail.getAuthor()));
						authorEntity.setDisplay(1);
						authorEntity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
						authorRepos.save(authorEntity);
					}
					entity.setAuthor(authorEntity);

					List<String> tagNames = detail.getTags();
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
					entity.setTags(tags);
					entity.setDisplay(1);
					entities.add(entity);
				}
			}
			newsRepository.saveAll(entities);
			if (entities != null && entities.size() > 0) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Thêm bài viết thành công!"),
						HttpStatus.OK);
			}
		}
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Thêm bài viết không thành công!"),
				HttpStatus.BAD_REQUEST);

	}

	@PutMapping(value = "/update/{id}")
	public ResponseEntity<ResponseMessage> update(@RequestBody NewsDto dto, @PathVariable Long id) {
		dto.setId(id);
		if (dto != null) {
			News entity = null;
			NewsCategory category = newsCategoryRepository.findOneBySlug(dto.getCategory_slug());
			if (dto.getId() != null) {
				entity = newsRepository.getById(dto.getId());
				entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (entity == null) {
				entity = new News();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
			}
			entity.setTitle(dto.getTitle());
			entity.setShort_description(dto.getShort_description());
			entity.setCategory(category);
			entity.setDisplay(1);
			entity = newsRepository.save(entity);
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
			News entity = newsRepository.getById(id);
			if (entity.getDisplay() == 1) {
				entity.setDisplay(0);
			} else {
				entity.setDisplay(1);
			}
			entity = newsRepository.save(entity);
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Ẩn bài viết thành công!"),
					HttpStatus.OK);
		}
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Ẩn bài viết không thành công!"),
				HttpStatus.BAD_REQUEST);
	}
}
