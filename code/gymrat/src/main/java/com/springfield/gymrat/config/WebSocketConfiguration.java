package com.springfield.gymrat.config;

import com.springfield.gymrat.websocket.WebSocketServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfiguration {

    // 创建ServerEndpointExporter实例
    @Bean
    @ConditionalOnMissingClass({
            "org.springframework.boot.test.context.SpringBootTest",
            "org.junit.jupiter.api.Test"
    })
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    // 创建WebSocketServer实例
    @Bean
    public WebSocketServer webSocketServer() {
        return new WebSocketServer();
    }
}
