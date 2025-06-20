package com.example.allinworks.module.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestCard {
    private String id;
    private String title;
    private String part;
    private String startDate;
    private String endDate;
    private String status;
    private Integer detailOrder;
    private String projectNo;
    private String userNo;
}
