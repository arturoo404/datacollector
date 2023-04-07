package com.arturoo404.DataCollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class DataCollectorApplication {

	//https://www.baeldung.com/mysql-jdbc-timezone-spring-boot
	public static void main(String[] args) {
		SpringApplication.run(DataCollectorApplication.class, args);
	}

}
