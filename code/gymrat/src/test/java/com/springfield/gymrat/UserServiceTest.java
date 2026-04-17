package com.springfield.gymrat;

import com.springfield.gymrat.dto.RegisterDTO;
import com.springfield.gymrat.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional  //测试完自动回滚
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 让测试按顺序执行
public class UserServiceTest {
    @Autowired
    private UserService userService;

    private static String testUsername = "test_user_" + System.currentTimeMillis();
    private static String testPhone = "13800138000";
    private static String testPassword = "123456Aa";

    @Test
    @Order(1)
    void testRegister() {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername(testUsername);
        dto.setPhone(testPhone);
        dto.setPassword(testPassword);

        // 执行注册
        Long userId = userService.register(dto);

        // 断言：确保返回了 ID
        assertNotNull(userId, "注册失败，用户ID为空");
        System.out.println("注册成功，测试用户ID: " + userId);
    }
}
