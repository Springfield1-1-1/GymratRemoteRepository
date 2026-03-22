package com.springfield.gymrat.dto;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;

@Data
public class UserQueryDTO {

    private String keyword;

    private Integer status;
}
