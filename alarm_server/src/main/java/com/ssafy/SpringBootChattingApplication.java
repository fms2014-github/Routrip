package com.ssafy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.ssafy")
public class SpringBootChattingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootChattingApplication.class, args);
	}

}
