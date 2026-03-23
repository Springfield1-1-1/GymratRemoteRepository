package com.springfield.gymrat.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GymStoreVO {

    private Long id;

    private String storeName;

    private String storeImage;

    private String city;

    private String address;

    private String phone;

    private Integer status;

    private List<CoachVO> coaches;
}
