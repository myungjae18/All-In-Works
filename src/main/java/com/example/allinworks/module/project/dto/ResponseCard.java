package com.example.allinworks.module.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseCard {
    private String id;
    private String title;
    private String part;
    private String startDate;
    private String endDate;
    private String status;
    private String userName;
    private String userNo;
    private Integer detailOrder;
}
