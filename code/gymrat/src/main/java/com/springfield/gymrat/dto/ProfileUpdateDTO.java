package com.springfield.gymrat.dto;

import lombok.Data;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProfileUpdateDTO {

    private Integer gender;

    private LocalDate birthday;

    @Min(value = 50, message = "身高不能低于 50cm")
    @Max(value = 250, message = "身高不能高于 250cm")
    private Integer height;

    @DecimalMin(value = "20.0", message = "体重不能低于 20kg")
    @DecimalMax(value = "300.0", message = "体重不能高于 300kg")
    private BigDecimal weight;

    @Size(max = 50, message = "健身目标长度不能超过 50 个字符")
    private String fitnessGoal;

    @Size(max = 200, message = "个人简介长度不能超过 200 个字符")
    private String bio;
}
