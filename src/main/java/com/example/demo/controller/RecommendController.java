package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PostDto;
import com.example.demo.dto.Recommend;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.repository.PostRepository;
import com.example.demo.utils.ContentBased;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/similar")
public class RecommendController {

	@Autowired
	private PostRepository postRepos;

	@GetMapping(value = "/{slug}")
	public ResponseEntity<?> getSimilarListProduct(@PathVariable String slug) {

		Post entity = postRepos.findOneBySlug(slug);

		List<List<String>> documents = new ArrayList<List<String>>();
		List<Post> entities = postRepos.getList();
		List<PostDto> dtos = new ArrayList<PostDto>();
		for (Post e : entities) {
			dtos.add(new PostDto(e));
		}

		for (PostDto item : dtos) {
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

		List<PostDto> result = new ArrayList<>();
		int result_size = list.size();
		if (result_size >= 3) {
			for (int i = 0; i < result_size; i++) {
				Post p = postRepos.getById(dtos.get(list.get(i).getIndex()).getId());
				PostDto pDto = new PostDto(p);
				result.add(pDto);
			}
			return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}

	}

}
