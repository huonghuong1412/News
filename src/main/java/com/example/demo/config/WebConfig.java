//package com.example.demo.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class WebConfig implements WebMvcConfigurer {
//
////	@Override
////	public void addCorsMappings(CorsRegistry registry) {
////		registry.addMapping("/api/**").allowedOrigins("http://localhost:3000")
////				.allowedMethods("PUT", "DELETE", "GET", "POST").allowedHeaders("header1", "header2", "header3")
////				.exposedHeaders("header1", "header2").allowCredentials(false).maxAge(3600);
////	}
//
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry
//		.addResourceHandler("/**")
//		.addResourceLocations("classpath:/static/")
//        .addResourceLocations("file:/image/");
//		;
//	}
//
//}
