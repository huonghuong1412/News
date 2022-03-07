package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NewsDto;
import com.example.demo.dto.Recommend;
import com.example.demo.model.News;
import com.example.demo.model.Tag;
import com.example.demo.repository.NewsRepository;
import com.example.demo.utils.ContentBased;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/similar")
public class SimilarController {

	@Autowired
	private NewsRepository repos;

	@GetMapping(value = "/{category}/{slug}")
	public ResponseEntity<?> getSimilarListProduct(@PathVariable String category, @PathVariable String slug) {

		News entity = repos.findOneBySlug(slug);

		List<List<String>> documents = new ArrayList<List<String>>();
		List<News> entities = repos.getList(category, PageRequest.of(0, 100, Sort.by("createdDate").descending()));
		List<NewsDto> dtos = new ArrayList<>();
		for (News e : entities) {
			dtos.add(new NewsDto(e));
		}

		for (NewsDto item : dtos) {
			List<String> listTags = new ArrayList<String>();
			for (int i = 0; i < item.getTag_slugs().size(); i++) {
				listTags.add(item.getTag_slugs().get(i));
			}
			documents.add(listTags);
		}
		List<String> tagList = new ArrayList<String>();
		List<String> tag_slugs = new ArrayList<String>();
		for (Tag tag : entity.getTags()) {
			tag_slugs.add(tag.getSlug());
		}

		for (int i = 0; i < tag_slugs.size(); i++) {
			tagList.add(tag_slugs.get(i));
		}
		List<Recommend> list = ContentBased.similarByTags(tagList, documents);

		List<NewsDto> result = new ArrayList<>();
		int result_size = list.size();
		if (result_size >= 3) {
			for (int i = 0; i < result_size; i++) {
				News p = repos.getById(dtos.get(list.get(i).getIndex()).getId());
				NewsDto pDto = new NewsDto(p);
				result.add(pDto);
			}
			return new ResponseEntity<List<NewsDto>>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}

	}

}
