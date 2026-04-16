package com.springfield.gymrat.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss") //自动去 application.yml 里找以 aliyun.oss 开头的配置项
@Slf4j
public class OssConfig {

    private String accessKeyId;

    private String accessKeySecret;

    private String endpoint;

    private String bucketName;

    @PostConstruct
    //生命周期回调方法，确认配置是否成功加载
    public void init() {
        log.info("OSS 配置加载 - endpoint: {}, bucketName: {}", endpoint, bucketName);
    }
}
