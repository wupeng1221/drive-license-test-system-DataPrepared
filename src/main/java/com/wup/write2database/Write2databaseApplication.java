package com.wup.write2database;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.wup.write2database.mapper")
public class Write2databaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(Write2databaseApplication.class, args);
    }

}
