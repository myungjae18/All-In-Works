package com.example.allinworks.module.approval.repository;

import com.example.allinworks.module.approval.domain.ApprovalDocForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocFormRepository  extends JpaRepository<ApprovalDocForm, String> {
    List<ApprovalDocForm> findByDelYn(String delYn);

    Optional<ApprovalDocForm> findByFormIdAndDelYn(String formId, String delYn);
}
