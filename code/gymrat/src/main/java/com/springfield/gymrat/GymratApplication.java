package com.springfield.gymrat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@MapperScan("com.springfield.gymrat.mapper")
public class GymratApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymratApplication.class, args);
    }

}