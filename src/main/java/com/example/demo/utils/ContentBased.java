package com.example.demo.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.Recommend;

@Service
public class ContentBased {

	public static List<Recommend> similarByTags(List<String> tags, List<List<String>> documents) {
		List<Double> result = new ArrayList<>();
		double queryDistance = 0;
		for (int i = 0; i < tags.size(); i++) {
			double bp = Math.pow(TF_IDFUtil.TF_IDF(tags, documents, tags.get(i)), 2); // binh phuong moi diem
			queryDistance += bp;
		}
		for (int i = 0; i < documents.size(); i++) {
			double dotProduct = 0, queryByDocument = 0, cosine_similarity = 0;
			for (int j = 0; j < tags.size(); j++) {
				dotProduct += TF_IDFUtil.TF_IDF(tags, documents, tags.get(j))
						* TF_IDFUtil.TF_IDF(documents.get(i), documents, tags.get(j));
				queryByDocument += Math.pow(TF_IDFUtil.TF_IDF(documents.get(i), documents, tags.get(j)), 2);
			}
			cosine_similarity = dotProduct / (Math.sqrt(queryDistance) * Math.sqrt(queryByDocument));
			result.add(cosine_similarity);
		}
		List<Recommend> items = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			if (!Double.isNaN(result.get(i))) {
				items.add(new Recommend(result.get(i), i));
			}
		}
		items.sort((Recommend o1, Recommend o2) -> o2.getValue() - o1.getValue() > 0 ? 1 : -1);
		List<Recommend> listResult = new ArrayList<>();
	
//		System.out.println(items.size());
		for (int i = 1; i < 4; i++) {
			listResult.add(new Recommend(items.get(i).getValue(), items.get(i).getIndex()));
		}
		return listResult;
	}
}
