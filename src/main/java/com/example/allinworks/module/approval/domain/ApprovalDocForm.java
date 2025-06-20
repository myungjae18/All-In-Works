package com.example.allinworks.module.approval.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@Table(name = "APPROVAL_DOC_FORM")
@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalDocForm {
    @Id
    @Column(name = "form_id")
    private String formId;

    @Column(name = "form_group")
    private String formGroup;

    @Column(name = "form_name")
    private String formName;

    @Column(name = "del_yn")
    private String delYn;
}
