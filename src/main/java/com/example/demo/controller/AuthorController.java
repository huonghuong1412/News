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

import com.example.demo.dto.AuthorDto;
import com.example.demo.dto.ResponseMessage;
import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.utils.Slug;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/post/author")
public class AuthorController {

	@Autowired
	private AuthorRepository authorRepos;

	@GetMapping("")
	public ResponseEntity<List<AuthorDto>> getAllNewsCategory() {
		List<AuthorDto> result = new ArrayList<>();
		List<Author> entities = authorRepos.findAll();
		for (Author entity : entities) {
			AuthorDto dto = new AuthorDto(entity);
			result.add(dto);
		}
		return new ResponseEntity<List<AuthorDto>>(result, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<Page<AuthorDto>> getAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
		Page<Author> list = authorRepos.findAll(PageRequest.of(page, limit, Sort.by(sortBy).descending()));
		Page<AuthorDto> result = list.map(tag -> new AuthorDto(tag));
		return new ResponseEntity<Page<AuthorDto>>(result, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AuthorDto> getById(@PathVariable Long id) {
		Author author = authorRepos.getById(id);
		AuthorDto result = new AuthorDto(author);
		return new ResponseEntity<AuthorDto>(result, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<ResponseMessage> create(@RequestBody AuthorDto dto) {
		if (dto != null) {
			Author entity = null;
			if (dto.getId() != null) {
				entity = authorRepos.getById(dto.getId());
				entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (entity == null) {
				entity = new Author();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
			}

			entity.setName(dto.getName());
			entity.setSlug(Slug.makeCode(dto.getName()));
			entity.setDisplay(1);
			entity = authorRepos.save(entity);

			if (entity != null) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Thêm tác giả thành công!"),
						HttpStatus.OK);
			}
		}

		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Thêm tác giả không thành công!"),
				HttpStatus.BAD_REQUEST);

	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ResponseMessage> update(@RequestBody AuthorDto dto, @PathVariable Long id) {
		dto.setId(id);
		if (dto != null) {
			Author entity = null;
			if (dto.getId() != null) {
				entity = authorRepos.getById(dto.getId());
				entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (entity == null) {
				entity = new Author();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
			}

			entity.setName(dto.getName());
			entity.setSlug(Slug.makeCode(dto.getName()));
			entity.setDisplay(1);
			entity = authorRepos.save(entity);

			if (entity != null) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Sửa tác giả thành công!"),
						HttpStatus.OK);
			}
		}

		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Sửa tác giả không thành công!"),
				HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ResponseMessage> delete(@PathVariable Long id) {
		if (id != null) {
			Author entity = authorRepos.getById(id);
			if (entity.getDisplay() == 1) {
				entity.setDisplay(0);
			} else {
				entity.setDisplay(1);
			}
			entity = authorRepos.save(entity);
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Xoá tác giả thành công!"),
					HttpStatus.OK);
		}
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Xoá tác giả không thành công!"),
				HttpStatus.BAD_REQUEST);
	}

}
