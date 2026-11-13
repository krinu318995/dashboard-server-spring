package com.dashboard.backend;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties; // 이 줄을 추가해 주세요
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties // 추가
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
