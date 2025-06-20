package com.example.allinworks.module.approval.repository;

import com.example.allinworks.module.approval.domain.ApprovalDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<ApprovalDoc, Integer> {

    @Query("SELECT COALESCE(MAX(documentNo), 0) FROM ApprovalDoc")
    int findMaxDocNo();

    Optional<ApprovalDoc> findByDocumentNoAndDelYn(int docNo, String delYn);

    List<ApprovalDoc> findByUserNoAndStatus(String userId, String status);

    List<ApprovalDoc> findByDelYn(String delYn);

    List<ApprovalDoc> findByUserNoAndStatusAndDelYn(String userId, String status, String delYn);
}
