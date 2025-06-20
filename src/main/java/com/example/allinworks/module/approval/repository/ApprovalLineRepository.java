package com.example.allinworks.module.approval.repository;

import com.example.allinworks.module.approval.domain.ApprovalLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Integer> {
    Optional<ApprovalLine> findByApprovalNoAndApprovalOrder(Integer docNo, String approvalOrder);

    List<ApprovalLine> findByUserNoAndStatus(String userNo, String status);

    Optional<ApprovalLine> findByLineNo(int lineNo);
}
