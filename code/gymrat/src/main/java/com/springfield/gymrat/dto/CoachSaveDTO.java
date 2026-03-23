package com.springfield.gymrat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoachSaveDTO {

    private Long id;

    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @NotBlank(message = "教练姓名不能为空")
    private String coachName;

    private String coachAvatar;

    private String specialty;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
