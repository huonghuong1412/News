package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface NewsTagService {

	@Transactional
	public Boolean deleteNews(Long newsId);

}
