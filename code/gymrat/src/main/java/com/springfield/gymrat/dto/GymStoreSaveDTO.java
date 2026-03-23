package com.springfield.gymrat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GymStoreSaveDTO {

    private Long id;

    private String storeName;

    private String storeImage;

    private String city;

    private String address;

    private String phone;

    private Integer status;
}
