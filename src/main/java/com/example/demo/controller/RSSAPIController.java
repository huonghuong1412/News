package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NewsRSSResponse;
import com.example.demo.repository.NewsRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/news/rss")
public class RSSAPIController {

	@Autowired
	private NewsRepository newsRepos;

	public static final String RSS_API_VNEXPRESS_MOST_VIEW = "https://vnexpress.net/rss/tin-noi-bat.rss";
	public static final String RSS_API_VIETNAMNET = "https://vietnamnet.vn/rss/";
	public static final String RSS_API_TUOITRE = "https://tuoitre.vn/rss/";
	public static final String RSS_API_THANHNIEN = "https://thanhnien.vn/rss/";

	public static Map<String, String> getEndPathUrl(String source, String category) {
		Map<String, String> result = new HashMap<String, String>();
		String rss = "";
		String url = "";
		if (source.equalsIgnoreCase("vietnamnet")) {
			url = RSS_API_VIETNAMNET;
			switch (category) {
			case "the-gioi":
				rss = "the-gioi.rss";
				break;
			case "thoi-su":
				rss = "thoi-su.rss";
				break;
			case "kinh-doanh":
				rss = "kinh-doanh.rss";
				break;
			case "giai-tri":
				rss = "giai-tri.rss";
				break;
			case "the-thao":
				rss = "the-thao.rss";
				break;
			case "suc-khoe":
				rss = "suc-khoe.rss";
				break;
			case "giao-duc":
				rss = "giao-duc.rss";
				break;
			case "khoa-hoc":
				rss = "cong-nghe.rss";
				break;
			default:
				break;
			}
		} else if (source.equalsIgnoreCase("tuoi-tre")) {
			url = RSS_API_TUOITRE;
			switch (category) {
			case "the-gioi":
				rss = "the-gioi.rss";
				break;
			case "thoi-su":
				rss = "thoi-su.rss";
				break;
			case "kinh-doanh":
				rss = "kinh-doanh.rss";
				break;
			case "giai-tri":
				rss = "giai-tri.rss";
				break;
			case "the-thao":
				rss = "the-thao.rss";
				break;
			case "suc-khoe":
				rss = "suc-khoe.rss";
				break;
			case "giao-duc":
				rss = "giao-duc.rss";
				break;
			case "khoa-hoc":
				rss = "khoa-hoc.rss";
				break;
			default:
				break;
			}
		} else if (source.equalsIgnoreCase("thanh-nien")) { // vnnet
			url = RSS_API_THANHNIEN;
			switch (category) {
			case "the-gioi":
				rss = "the-gioi-66.rss";
				break;
			case "thoi-su":
				rss = "thoi-su-4.rss";
				break;
			case "kinh-doanh":
				rss = "tai-chinh-kinh-doanh-49.rss";
				break;
			case "giai-tri":
				rss = "giai-tri-285.rss";
				break;
			case "the-thao":
				rss = "the-thao-318.rss";
				break;
			case "suc-khoe":
				rss = "suc-khoe-65.rss";
				break;
			case "giao-duc":
				rss = "giao-duc-26.rss";
				break;
			case "khoa-hoc":
				rss = "cong-nghe-12.rss";
				break;
			default:
				break;
			}
		} else {
			rss = "";
		}
		result.put("url", url);
		result.put("rss", rss);
		return result;
	}

	@GetMapping(value = "")
	public ResponseEntity<Object> getRssNewsVnExpress(@RequestParam(name = "source", defaultValue = "") String source,
			@RequestParam(name = "category", defaultValue = "") String category) throws IOException {

		if (category.equals("") || source.equals("")) {
			return new ResponseEntity<Object>(null, HttpStatus.OK);
		}

		Map<String, String> endpoint = getEndPathUrl(source, category);

		URL url = new URL(endpoint.get("url") + endpoint.get("rss"));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output;
		StringBuilder response = new StringBuilder();
		while ((output = br.readLine()) != null) {
			response.append(output);
		}
		JSONObject jsonObj = XML.toJSONObject(response.toString());

		if (jsonObj.isEmpty()) {
			return new ResponseEntity<Object>(jsonObj.toMap(), HttpStatus.OK);
		} else {
			JSONObject rss = new JSONObject();
			if (source.equalsIgnoreCase("vietnamnet")) {
//				rss = jsonObj.getJSONObject("vnn").getJSONObject("rss");
				rss = jsonObj.getJSONObject("rss");
			} else {
				rss = jsonObj.getJSONObject("rss");
			}
			JSONObject chanel = rss.getJSONObject("channel"); // 60 items
			JSONArray items = new JSONArray();
			if (source.equalsIgnoreCase("vietnamnet")) {
//				items = chanel.getJSONObject("vnn").getJSONArray("item");
				items = chanel.getJSONArray("item");
			} else {
				items = chanel.getJSONArray("item");
			}

			JSONObject result = new JSONObject();
			List<Object> obj = new ArrayList<Object>();
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				String desc = item.getString("description");
				String title = item.getString("title");
				String link = item.getString("link");
				String image = "";
				if (source.equalsIgnoreCase("thanh-nien")) {
					image = item.getString("image");
				} else if (source.equalsIgnoreCase("vietnamnet")) {
					image = item.getJSONObject("media:content").getString("url");
				} else {
					image = "";
				}
				if (!newsRepos.existsByUrl(link)) {
					String html = "<html><head><title>Document</title></head>" + "<body>" + desc + "</body></html>";
					Document document = Jsoup.parse(html);
					Elements img = document.select("img");
					if (source.equalsIgnoreCase("thanh-nien")) {
						obj.add(new NewsRSSResponse(title, document.body().text(), image, link));
					} else if (source.equalsIgnoreCase("vietnamnet")) {
						obj.add(new NewsRSSResponse(title, document.body().text(), image, link));
					} else {
						obj.add(new NewsRSSResponse(title, document.body().text(), img.attr("src"), link));
					}

				}
			}
			result.put("items", obj);
			return new ResponseEntity<Object>(result.toMap(), HttpStatus.OK);
		}
	}

	@GetMapping(value = "/most-viewed")
	public ResponseEntity<Object> getMostView() throws IOException {

		URL url = new URL(RSS_API_VNEXPRESS_MOST_VIEW);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output;
		StringBuilder response = new StringBuilder();
		while ((output = br.readLine()) != null) {
			response.append(output);
		}
		JSONObject jsonObj = XML.toJSONObject(response.toString());

		JSONArray items = jsonObj.getJSONObject("rss").getJSONObject("channel").getJSONArray("item"); // 60 items
		JSONObject result = new JSONObject();

		List<Object> obj = new ArrayList<Object>();
		for (int i = 0; i < 20; i++) {
			JSONObject item = items.getJSONObject(i);
			String desc = item.getString("description");
			String title = item.getString("title");
			String link = item.getString("link");
			if (!newsRepos.existsByUrl(link)) {
				String html = "<html><head><title>Document</title></head>" + "<body>" + desc + "</body></html>";
				Document document = Jsoup.parse(html);
				Elements img = document.select("img");
				obj.add(new NewsRSSResponse(title, document.body().text(), img.attr("src"), link));
			}
		}
		result.put("items", obj);
		return new ResponseEntity<Object>(result.toMap(), HttpStatus.OK);
	}

}
