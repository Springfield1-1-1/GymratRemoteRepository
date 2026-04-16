package com.springfield.gymrat.config;

import com.springfield.gymrat.websocket.WebSocketServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfiguration {

    // 扫描使用@ServerEndpoint注解声明的WebSocket endpoint
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    // 创建WebSocketServer实例
    @Bean
    public WebSocketServer webSocketServer() {
        return new WebSocketServer();
    }
}
