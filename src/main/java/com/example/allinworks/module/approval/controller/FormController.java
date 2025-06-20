package com.example.allinworks.module.approval.controller;

import com.example.allinworks.common.Common;
import com.example.allinworks.module.approval.domain.ApprovalDocForm;
import com.example.allinworks.module.approval.dto.DocForm;
import com.example.allinworks.module.approval.dto.FormRequest;
import com.example.allinworks.module.approval.repository.DocFormRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/form")
public class FormController {
    private final DocFormRepository docFormRepository;

    //전자 결재 양식 데이터 insert용
    @PostMapping("/formInsert")
    public ResponseEntity<?> dataInsert(@RequestBody List<Map<String, Object>> formDataList) {

        for (Map<String, Object> formData : formDataList) {
            ApprovalDocForm form = new ApprovalDocForm();
            String formId = Common.getNumber();
            form.setFormId(formId);
            form.setFormName((String) formData.get("formName"));
            form.setFormGroup((String) formData.get("formGroup"));
            form.setDelYn("N");
            docFormRepository.save(form);
        }

        return ResponseEntity.ok(formDataList.size());
    }

}
