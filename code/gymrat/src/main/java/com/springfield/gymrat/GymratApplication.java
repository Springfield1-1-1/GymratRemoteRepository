package com.springfield.gymrat;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching  // 开启缓存功能
@SpringBootApplication
@EnableAdminServer
@MapperScan("com.springfield.gymrat.mapper")    // 扫描mapper接口
public class GymratApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymratApplication.class, args);
    }

}