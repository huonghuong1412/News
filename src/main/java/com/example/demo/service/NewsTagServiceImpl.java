package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.News;
import com.example.demo.model.Tag;
import com.example.demo.repository.NewsRepository;

@Service
public class NewsTagServiceImpl implements NewsTagService {

	@Autowired
	private NewsRepository newsRepository;
	
	@Override
	public Boolean deleteNews(Long newsId) {
		if (newsId != null) {
			News entity = newsRepository.getById(newsId);
			List<Tag> tags = entity.getTags();
			for (Tag tag : tags) {
				tag.getNews().remove(entity);
			}
			newsRepository.deleteById(newsId);
			return true;
		}
		return false;
	}

}
