package com.springfield.gymrat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {

    // 存储所有在线的 Session，key 为 sid
    private static final Map<String, Session> SESSION_POOL = new ConcurrentHashMap<>();

    // 存储所有在线客服的 ID
    private static final Map<String, String> ONLINE_CUSTOMERS = new ConcurrentHashMap<>();

    // ObjectMapper 用于 JSON 序列化
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 连接建立成功调用
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("[WebSocket] 用户连接成功，sid: {}", sid);
        SESSION_POOL.put(sid, session);

        // 如果是客服，记录到在线客服列表
        if (sid.startsWith("customer_")) {
            ONLINE_CUSTOMERS.put(sid, sid);
            log.info("[WebSocket] 客服上线");
        }

        // 广播在线人数
        broadcastOnlineCount();
    }

    /**
     * 收到客户端消息后调用
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("[WebSocket] 收到来自 {} 的消息：{}", sid, message);

        try {
            // 解析消息
            Map<String, Object> msg = objectMapper.readValue(message, Map.class);
            String type = (String) msg.get("type");
            String content = (String) msg.get("content");
            String targetId = (String) msg.get("targetId");

            // 根据消息类型处理
            if ("chat".equals(type)) {
                // 聊天消息
                handleChatMessage(sid, targetId, content);
            } else if ("typing".equals(type)) {
                // 正在输入
                sendTypingStatus(targetId, sid);
            }
        } catch (Exception e) {
            log.error("[WebSocket] 消息处理失败", e);
            sendMessage(sid, "系统错误：" + e.getMessage());
        }
    }

    /**
     * 连接关闭调用
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("[WebSocket] 用户断开连接，sid: {}", sid);
        SESSION_POOL.remove(sid);
        ONLINE_CUSTOMERS.remove(sid);

        // 广播在线人数
        broadcastOnlineCount();
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("[WebSocket] 发生错误", error);
    }

    /**
     * 处理聊天消息
     */
    private void handleChatMessage(String fromId, String toId, String content) {
        if (toId == null || !SESSION_POOL.containsKey(toId)) {
            log.warn("[WebSocket] 目标用户不在线，from: {}, to: {}", fromId, toId);
            return;
        }

        try {
            // 构建消息对象
            Map<String, Object> message = Map.of(
                    "type", "chat",
                    "fromId", fromId,
                    "content", content,
                    "timestamp", System.currentTimeMillis()
            );

            // 发送给目标用户
            sendMessage(toId, objectMapper.writeValueAsString(message));

            // 如果是客服回复用户，也给自己发一份（用于显示在聊天窗口）
            if (fromId.startsWith("customer_")) {
                sendMessage(fromId, objectMapper.writeValueAsString(Map.of(
                        "type", "chat_sent",
                        "toId", toId,
                        "content", content,
                        "timestamp", System.currentTimeMillis()
                )));
            }
        } catch (Exception e) {
            log.error("[WebSocket] 发送消息失败", e);
        }
    }

    /**
     * 发送正在输入状态
     */
    private void sendTypingStatus(String toId, String typingUserId) {
        if (toId != null && SESSION_POOL.containsKey(toId)) {
            try {
                Map<String, Object> message = Map.of(
                        "type", "typing",
                        "userId", typingUserId,
                        "timestamp", System.currentTimeMillis()
                );
                sendMessage(toId, objectMapper.writeValueAsString(message));
            } catch (Exception e) {
                log.error("[WebSocket] 发送输入状态失败", e);
            }
        }
    }

    /**
     * 广播在线人数
     */
    private void broadcastOnlineCount() {
        try {
            Map<String, Object> message = Map.of(
                    "type", "online_count",
                    "count", SESSION_POOL.size(),
                    "customers", ONLINE_CUSTOMERS.size(),
                    "timestamp", System.currentTimeMillis()
            );

            String json = objectMapper.writeValueAsString(message);
            for (String sid : SESSION_POOL.keySet()) {
                sendMessage(sid, json);
            }
        } catch (Exception e) {
            log.error("[WebSocket] 广播在线人数失败", e);
        }
    }

    /**
     * 群发消息
     */
    public void broadcast(String message) {
        for (String sid : SESSION_POOL.keySet()) {
            sendMessage(sid, message);
        }
    }

    /**
     * 给指定用户发送消息
     */
    public void sendMessage(String sid, String message) {
        Session session = SESSION_POOL.get(sid);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("[WebSocket] 发送消息失败，sid: {}", sid, e);
                SESSION_POOL.remove(sid);
            }
        }
    }

    /**
     * 获取在线客服列表
     */
    public static Map<String, String> getOnlineCustomers() {
        return ONLINE_CUSTOMERS;
    }

    /**
     * 判断用户是否在线
     */
    public static boolean isOnline(String sid) {
        return SESSION_POOL.containsKey(sid);
    }
}
