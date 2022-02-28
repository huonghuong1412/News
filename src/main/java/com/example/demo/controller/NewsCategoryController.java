package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.example.demo.dto.NewsCategoryDto;
import com.example.demo.dto.ResponseMessage;
import com.example.demo.model.NewsCategory;
import com.example.demo.repository.NewsCategoryRepository;
import com.example.demo.utils.Slug;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/news/category")
public class NewsCategoryController {

	@Autowired
	private NewsCategoryRepository newsCategoryRepository;

	@GetMapping("")
	public ResponseEntity<List<NewsCategoryDto>> getAllNewsCategory() {
		List<NewsCategoryDto> result = new ArrayList<>();
		List<NewsCategory> entities = newsCategoryRepository.findAll();
		for (NewsCategory entity : entities) {
			NewsCategoryDto dto = new NewsCategoryDto(entity);
			result.add(dto);
		}
		return new ResponseEntity<List<NewsCategoryDto>>(result, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<Page<NewsCategoryDto>> getAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
		Page<NewsCategory> list = newsCategoryRepository
				.findAll(PageRequest.of(page, limit, Sort.by(sortBy).descending()));
		Page<NewsCategoryDto> result = list.map(tag -> new NewsCategoryDto(tag));
		return new ResponseEntity<Page<NewsCategoryDto>>(result, HttpStatus.OK);
	}
	
	@GetMapping("/{id}") 
	public ResponseEntity<NewsCategoryDto> getById(@PathVariable Long id) {
		NewsCategory news = newsCategoryRepository.getById(id);
		NewsCategoryDto result = new NewsCategoryDto(news);
		return new ResponseEntity<NewsCategoryDto>(result, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<ResponseMessage> create(@RequestBody NewsCategoryDto dto) {
		if (dto != null) {
			NewsCategory entity = null;
			if (dto.getId() != null) {
				entity = newsCategoryRepository.getById(dto.getId());
				entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (entity == null) {
				entity = new NewsCategory();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
			}

			entity.setName(dto.getName());
			entity.setSlug(Slug.makeCode(dto.getName()));
			entity.setDisplay(1);
			entity = newsCategoryRepository.save(entity);

			if (entity != null) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Thêm danh mục thành công!"),
						HttpStatus.OK);
			}
		}

		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Thêm danh mục không thành công!"),
				HttpStatus.BAD_REQUEST);

	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ResponseMessage> update(@RequestBody NewsCategoryDto dto, @PathVariable Long id) {
		dto.setId(id);
		if (dto != null) {
			NewsCategory entity = null;
			if (dto.getId() != null) {
				entity = newsCategoryRepository.getById(dto.getId());
				entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (entity == null) {
				entity = new NewsCategory();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
			}

			entity.setName(dto.getName());
			entity.setSlug(Slug.makeCode(dto.getName()));
			entity.setDisplay(1);
			entity = newsCategoryRepository.save(entity);

			if (entity != null) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Sửa danh mục thành công!"),
						HttpStatus.OK);
			}
		}

		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Sửa danh mục không thành công!"),
				HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ResponseMessage> delete(@PathVariable Long id) {
		if (id != null) {
			NewsCategory entity = newsCategoryRepository.getById(id);
			if (entity.getDisplay() == 1) {
				entity.setDisplay(0);
			} else {
				entity.setDisplay(1);
			}
			entity = newsCategoryRepository.save(entity);
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Xoá danh mục thành công!"),
					HttpStatus.OK);
		}
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Xoá danh mục không thành công!"),
				HttpStatus.BAD_REQUEST);
	}

}
