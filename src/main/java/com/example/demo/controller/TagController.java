package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseMessage;
import com.example.demo.dto.TagDto;
import com.example.demo.model.Tag;
import com.example.demo.repository.TagsRepository;
import com.example.demo.utils.Slug;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/news/tag")
public class TagController {

	@Autowired
	private TagsRepository tagRepository;

	@GetMapping("")
	public ResponseEntity<Page<TagDto>> getAllNewsTag(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "24") Integer limit,
			@RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
		Page<Tag> list = tagRepository.getList(PageRequest.of(page, limit, Sort.by(sortBy).descending()));

		Page<TagDto> result = list.map(tag -> new TagDto(tag));
		return new ResponseEntity<Page<TagDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<TagDto> getById(@PathVariable Long id) {
		Tag tag = tagRepository.getById(id);
		TagDto result = new TagDto(tag);
		return new ResponseEntity<TagDto>(result, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<ResponseMessage> create(@RequestBody TagDto dto) {
		if (dto != null) {
			Tag entity = null;
			if (dto.getId() != null) {
				entity = tagRepository.getById(dto.getId());
				entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (entity == null) {
				entity = new Tag();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
			}

			entity.setName(dto.getName());
			entity.setSlug(Slug.makeCode(dto.getName()));
			entity = tagRepository.save(entity);

			if (entity != null) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Thêm tag thành công!"),
						HttpStatus.OK);
			}
		}

		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Thêm tag không thành công!"),
				HttpStatus.BAD_REQUEST);

	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ResponseMessage> update(@RequestBody TagDto dto, @PathVariable Long id) {
		dto.setId(id);
		if (dto != null) {
			Tag entity = null;
			if (dto.getId() != null) {
				entity = tagRepository.getById(dto.getId());
				entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (entity == null) {
				entity = new Tag();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
			}

			entity.setName(dto.getName());
			entity.setSlug(Slug.makeCode(dto.getName()));
			entity = tagRepository.save(entity);

			if (entity != null) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Sửa tag thành công!"),
						HttpStatus.OK);
			}
		}

		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Sửa tag không thành công!"),
				HttpStatus.BAD_REQUEST);
	}

//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<ResponseMessage> delete(@PathVariable Long id) {
//		if (id != null) {
//			Tag entity = tagRepository.getById(id);
//			if (entity.getDisplay() == 1) {
//				entity.setDisplay(0);
//			} else {
//				entity.setDisplay(1);
//			}
//			entity = newsGenreRepository.save(entity);
//			return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Xoá thể loại thành công!"),
//					HttpStatus.OK);
//		}
//		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Xoá thể loại không thành công!"),
//				HttpStatus.BAD_REQUEST);
//	}

}
