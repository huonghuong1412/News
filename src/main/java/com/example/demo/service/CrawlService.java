package com.example.demo.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.constant.ThanhnienConstant;
import com.example.demo.constant.TuoitreConstant;
import com.example.demo.constant.VietnamnetConstant;
import com.example.demo.constant.VnexpressConstant;
import com.example.demo.dto.NewsCrawlDetail;
import com.example.demo.repository.NewsSourceRepository;

@Service
public class CrawlService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private NewsSourceRepository newSourceRepos;

	public NewsCrawlDetail getData(String source, String url) throws IOException {
		
		JSONObject response = new JSONObject();
		Document doc = Jsoup.connect(url).timeout(60000).get();
		String responseTitle = "";
		String responseShortDesc = "";
		String responseAuthor = "";
		List<String> responseTags = new ArrayList<>();
		String responseContent = "";

		Elements body, contentElement, paragrapths = null;
		String content = "";
		String title, short_description, author;
		String[] parts;
		List<String> tags = new ArrayList<>();
		switch (source) {
		case "thanh-nien":
			body = doc.select(ThanhnienConstant.BODY_THANHNIEN);
			body.select("#adsWeb_AdsArticleAfterBody").remove();
			body.select(".morenews").remove();
			title = body.select(ThanhnienConstant.TITLE_THANHNIEN).text();
			short_description = body.select(ThanhnienConstant.SHORT_DESCRIPTION_THANHNIEN).text();
			author = body.select(ThanhnienConstant.AUTHOR_THANHNIEN).text();
			contentElement = body.select(ThanhnienConstant.CONTENT_THANHNIEN);
			contentElement.select("#adsWeb_AdsArticleMiddle").remove();
			contentElement.select("table.video").remove();
			contentElement.select("table").removeAttr("align");
			contentElement.select("table").attr("class", "image-container");
			contentElement.select(".cms-note.notebox.ncenter").remove();

			paragrapths = contentElement.select("p");
			for (Element e : paragrapths) {
				Elements aTag = e.select("a");
				aTag.attr("target", "_blank");
			}

			contentElement.select(".image-container").select("tbody tr td:first-child").attr("class", "image");
			contentElement.select(".image-container").select("tbody tr td:last-child").attr("class", "image_desc");

			Elements images = contentElement.select("table img");
			images.removeAttr("data-image-id");
			images.removeAttr("data-width");
			images.removeAttr("data-height");
			for (int i = 1; i < images.size(); i++) {
				images.get(i).attr("src", images.get(i).getElementsByTag("img").attr("data-src"));
			}

			tags = new ArrayList<>();
			for (Element e : doc.select(ThanhnienConstant.TAGS_THANHNIEN)) {
				tags.add(e.text().replace("#", ""));
			}
			for (Element e : contentElement) {
				content += e.html();
			}
			responseTitle += title;
			responseShortDesc += short_description;
			parts = author.split("[\\(\\)]");
			responseAuthor += parts[0].trim();
			responseContent += content;
			for (String e : tags) {
				responseTags.add(e);
			}
			break;
		case "vnexpress":
			body = doc.select(VnexpressConstant.BODY_VNEXPRESS);
			title = body.select(VnexpressConstant.TITLE_VNEXPRESS).text();
			short_description = body.select(VnexpressConstant.SHORT_DESCRIPTION_VNEXPRESS).text();
			author = body.select(VnexpressConstant.AUTHOR_VNEXPRESS).last().text();
			body.select(VnexpressConstant.CONTENT_VNEXPRESS).select(".list-news").remove();
			
			contentElement = body.select(VnexpressConstant.CONTENT_VNEXPRESS);
			contentElement.select(".list_link").remove();

			paragrapths = contentElement.select("p");
			paragrapths.forEach((p) -> {
                p.removeAttr("class");
            });
			for (Element e : paragrapths) {
				Elements aTag = e.select("a");
				aTag.attr("target", "_blank");
			}

			Elements figures = contentElement.select("figure");

            for (Element figure : figures) {
                Elements imagesVnExpress = figure.select("img");
                Elements figcaptions = figure.select("figcaption");
                figures.attr("class", "image-container");
                figures.removeAttr("data-size");
                figures.removeAttr("itemprop");
                figures.removeAttr("itemscope");
                figures.removeAttr("itemtype");
                figures.select("meta").remove();
                figures.select("figcaption").removeAttr("itemprop");
                for (Element image : imagesVnExpress) {
                    image.removeAttr("itemprop");
                    image.removeAttr("intrinsicsize");
                    image.removeAttr("itemprop");
                    image.removeAttr("style");
                    image.attr("src", image.getElementsByTag("img").attr("data-src"));
                }
                figure.html(imagesVnExpress.toString() + figcaptions.toString());
            }
            contentElement.select("p").last().remove();
			
			tags = new ArrayList<>();
			Elements metaTags = doc.select("meta[name=its_tag]");
			for (Element e : metaTags) {
				tags.add(e.attr("content"));
			}
			for (Element e : contentElement) {
				content += e.html();
			}
			responseTitle += title;
			responseShortDesc += short_description;
			parts = author.split("[\\(\\)]");
			responseAuthor += parts[0].trim();
			responseContent += content;
			for (String e : tags) {
				responseTags.add(e);
			}
			break;
		case "tuoi-tre":
			body = doc.select(TuoitreConstant.BODY_TUOITRE);
			title = body.select(TuoitreConstant.TITLE_TUOITRE).text();
			short_description = body.select(TuoitreConstant.SHORT_DESCRIPTION_TUOITRE).text();
			author = body.select(TuoitreConstant.AUTHOR_TUOITRE).text();

			body.select(TuoitreConstant.CONTENT_TUOITRE).select(".VCSortableInPreviewMode")
					.attr("class", "image-container").removeAttr("style");
			body.select(TuoitreConstant.CONTENT_TUOITRE).select(".image-container[type='RelatedOneNews']").remove();

			contentElement = body.select(TuoitreConstant.CONTENT_TUOITRE);

			for (Element e : contentElement) {
				Elements p = e.select(".PhotoCMS_Caption p");
				p.removeAttr("data-placeholder").removeAttr("class");
			}

			Elements imagesContainer = contentElement.select(".image-container[type='Photo'] div img");
			Elements captionContainer = contentElement.select(".image-container[type='Photo'] .PhotoCMS_Caption");
			imagesContainer.removeAttr("id").removeAttr("w").removeAttr("h").removeAttr("photoid").removeAttr("width")
					.removeAttr("height").removeAttr("data-original");
			captionContainer.attr("class", "image_desc");

			paragrapths = contentElement.select("p");
			paragrapths.removeAttr("class");
			for (Element e : paragrapths) {
				Elements aTag = e.select("a");
				aTag.attr("target", "_blank");
			}
			tags = new ArrayList<>();
			for (Element e : doc.select(TuoitreConstant.TAGS_TUOITRE)) {
				tags.add(e.text());
			}
			for (Element e : contentElement) {
				content += e.html();
			}
			responseTitle += title;
			responseShortDesc += short_description;
			parts = author.split("[\\(\\)]");
			responseAuthor += parts[0].trim();
			responseContent += content;
			for (String e : tags) {
				responseTags.add(e);
			}
			break;
		case "vietnamnet":
			body = doc.select(VietnamnetConstant.BODY_VIETNAMNET);
			title = body.select(VietnamnetConstant.TITLE_VIETNAMNET).text();
			short_description = body.select(VietnamnetConstant.SHORT_DESCRIPTION_VIETNAMNET).text();
			body.select(".article-relate").remove();
			body.select(".ArticleLead").remove();
			body.select(".VnnAdsPos[data-pos='vt_article_inread']").parents().first().remove();
			body.select(".inner-article").remove();
			body.select(".template-ReferToMore.right").remove();
			contentElement = body.select(VietnamnetConstant.CONTENT_VIETNAMNET);
			contentElement.select(".ImageBox.ImageCenterBox").attr("class", "image-container");
			contentElement.select(".image-container").select("tbody tr td:first-child").attr("class", "image");
			contentElement.select(".image-container").select("tbody tr td:last-child").attr("class", "image_desc");
			paragrapths = contentElement.select("p");
			for (Element e : paragrapths) {
				Elements aTag = e.select("a");
				aTag.attr("target", "_blank");
			}
			author = body.select(VietnamnetConstant.CONTENT_VIETNAMNET).select("p").last().text();
			tags = new ArrayList<>();
			for (Element e : doc.select(VietnamnetConstant.TAGS_VIETNAMNET)) {
				tags.add(e.select("li a").text());
			}
			for (Element e : contentElement) {
				content += e.html();
			}
			responseTitle += title;
			responseShortDesc += short_description;
			parts = author.split("[\\(\\)]");
			responseAuthor += parts[0].trim();
			responseContent += content;
			for (String e : tags) {
				responseTags.add(e);
			}
			break;
		default:
			break;
		}
		response.put("title", responseTitle);
		response.put("short_description", responseShortDesc);
		response.put("author", responseAuthor);
		response.put("content", responseContent);
		response.put("tags", responseTags);

		if (newSourceRepos.findOneBySlug(source) == null) {
			return null;
		} else {
			return new NewsCrawlDetail(responseTitle, responseContent, responseShortDesc, responseAuthor, responseTags);
		}

	}

}
