package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseMessage;
import com.example.demo.dto.SourceDto;
import com.example.demo.model.Source;
import com.example.demo.repository.SourceRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/news/source")
public class SourceController {

	@Autowired
	private SourceRepository sourceRepos;

	@GetMapping("/{id}")
	public ResponseEntity<SourceDto> getById(@PathVariable Long id) {
		Source news = sourceRepos.getById(id);
		SourceDto result = new SourceDto(news);
		return new ResponseEntity<SourceDto>(result, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<ResponseMessage> create(@RequestBody SourceDto dto) {
		if (dto != null) {
			Source entity = null;
			if (sourceRepos.existsBySlug(dto.getSlug())) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Nguồn đã tồn tại!"),
						HttpStatus.OK);
			} else {
				entity = new Source();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
				entity.setName(dto.getName());
				entity.setSlug(dto.getSlug());
				entity.setUrl_logo(dto.getUrl_logo());
				entity = sourceRepos.save(entity);

				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Thêm danh mục thành công!"),
						HttpStatus.OK);
			}
		}
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Thêm danh mục không thành công!"),
				HttpStatus.BAD_REQUEST);

	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ResponseMessage> update(@RequestBody SourceDto dto, @PathVariable Long id) {
		dto.setId(id);
		if (dto != null) {
			Source entity = null;
			if (dto.getId() != null) {
				entity = sourceRepos.getById(dto.getId());
				entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
			}
			if (entity == null) {
				entity = new Source();
				entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
			}

			entity.setName(dto.getName());
			entity.setSlug(dto.getSlug());
			entity.setUrl_logo(dto.getUrl_logo());
			entity = sourceRepos.save(entity);

			if (entity != null) {
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Sửa danh mục thành công!"),
						HttpStatus.OK);
			}
		}

		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Sửa danh mục không thành công!"),
				HttpStatus.BAD_REQUEST);
	}

//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<ResponseMessage> delete(@PathVariable Long id) {
//		if (id != null) {
//			Source entity = sourceRepos.getById(id);
//			if (entity.getDisplay() == 1) {
//				entity.setDisplay(0);
//			} else {
//				entity.setDisplay(1);
//			}
//			entity = sourceRepos.save(entity);
//			return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Xoá danh mục thành công!"),
//					HttpStatus.OK);
//		}
//		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Xoá danh mục không thành công!"),
//				HttpStatus.BAD_REQUEST);
//	}

}
