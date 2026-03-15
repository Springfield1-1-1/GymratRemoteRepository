package com.springfield.gymrat.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Slf4j
public class OssConfig {

    private String accessKeyId;

    private String accessKeySecret;

    private String endpoint;

    private String bucketName;

    @PostConstruct
    public void init() {
        log.info("OSS 配置加载 - endpoint: {}, bucketName: {}", endpoint, bucketName);
    }
}
