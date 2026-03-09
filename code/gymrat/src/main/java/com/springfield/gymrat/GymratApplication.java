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
//TODO: 解决版本兼容问题
//TODO: 检查并接口测试登录功能，成功后提交至仓库