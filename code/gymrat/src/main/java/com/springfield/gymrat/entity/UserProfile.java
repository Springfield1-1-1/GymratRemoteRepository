package com.springfield.gymrat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user_profiles")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;

    private String nickname;

    private Integer gender = 0;

    private LocalDate birthday;

    private Integer height;

    private BigDecimal weight;

    private String fitnessGoal;

    private Integer experienceLevel = 1;

    private String bio = "";

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;
}
