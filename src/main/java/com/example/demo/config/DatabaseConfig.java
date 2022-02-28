package com.example.demo.config;

import javax.activation.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("postgres://mfwqzomevzmfaw:f952aef7b65a4523b507fab57c88a485912422464140274979566d7634807a0b@ec2-18-235-55-95.compute-1.amazonaws.com:5432/da1dt0gvbo68dp");
		return (DataSource) new HikariDataSource(config);
	}

}