package com.example.allinworks.module.project.mapper;

import com.example.allinworks.module.project.domain.ProjectDetail;
import com.example.allinworks.module.project.domain.ProjectStatus;
import com.example.allinworks.module.project.dto.RequestCard;
import com.example.allinworks.module.project.dto.ResponseCard;

import java.time.LocalDate;
import java.util.Optional;

public class ProjectDetailMapper {
    public static ResponseCard detailToCard(ProjectDetail detail) {
        ResponseCard card = new ResponseCard();

        card.setId(String.valueOf(detail.getDetailNo()));
        card.setTitle(detail.getTitle());
        card.setPart(detail.getPart());
        card.setStartDate(detail.getStartDate().toString());
        card.setEndDate(detail.getEndDate().toString());
        card.setStatus(detail.getStatus().name());
        card.setUserName(detail.getUser().getUserName());
        card.setUserNo(detail.getUser().getUserNo());
        card.setDetailOrder(detail.getDetailOrder());

        return card;
    }

    public static ProjectDetail cardToDetail(RequestCard card) {
        return ProjectDetail.builder()
                .title(card.getTitle())
                .part(card.getPart())
                .startDate(LocalDate.parse(card.getStartDate()))
                .endDate(LocalDate.parse(card.getEndDate()))
                .status(ProjectStatus.fromLabel(card.getStatus()))
                .detailOrder(card.getDetailOrder())
                .build();
    }
}
