package com.springfield.gymrat.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserProfileVO {

    private Long userId;

    private String username;

    private String avatarUrl;

    private Integer gender;

    private LocalDate birthday;

    private Integer height;

    private BigDecimal weight;

    private String fitnessGoal;

    private String bio;
}
