package com.springfield.gymrat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.springfield.gymrat.mapper")
public class GymratApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymratApplication.class, args);
    }

}
