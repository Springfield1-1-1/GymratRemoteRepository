package com.springfield.gymrat.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachVO {

    private Long id;

    private String coachName;

    private String coachAvatar;

    private String specialty;

}
