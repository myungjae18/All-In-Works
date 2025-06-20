package com.example.allinworks.module.approval.controller;

import com.example.allinworks.module.approval.dto.*;
import com.example.allinworks.module.approval.service.ApprovalService;
import com.example.allinworks.module.schedule.dto.UserDto;
import com.example.allinworks.module.user.domain.User;
import com.example.allinworks.module.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/approval")
@AllArgsConstructor
public class ApprovalController {
    private final ApprovalService approvalService;
    private final UserService userService;


    /**
     * 전자 결재 메인페이지 진입
     */
    @GetMapping({"", "/"})
    public String docMain(Model model) {
        List<DocResponse> documents = approvalService.getAllDocList();
        model.addAttribute("documents", documents);
        return "approval/main";
    }



    /**
     * 전자 결재 내 문서함
     */
    @GetMapping({"/getList"})
    public String getUserDocList(Model model, @RequestParam String userId, @RequestParam String status) {
        List<DocResponse> docList = approvalService.getUserDocList(userId, status);
        model.addAttribute("documents", docList);
        model.addAttribute("status", status);
        return "approval/main";
    }


    /**
     * 미결함
     */
    @GetMapping({"/getConfirmList"})
    public String getConfirmDocList(Model model, @RequestParam String userId) {
        List<DocResponse> docList = approvalService.getConfirmDocList(userId);
        model.addAttribute("documents", docList);
        model.addAttribute("status", "50");
        return "approval/main";
    }



    /**
     * 전자 결재 양식 리스트
     */
    @GetMapping("/formList")
    public String formList(Model model) {
        Map<String, List<DocForm>> formList = approvalService.getFormList();
        model.addAttribute("formList", formList);
        return "approval/approval-forms";
    }


    /**
     * 결재 문서 전체 리스트 조회
     */
    @GetMapping("/list")
    public String docList(Model model) {
//        List<DocResponse> documents;
//        documents = approvalService.getAllDocList();
//        model.addAttribute("documents", documents);
        return "approval/doc-main";
    }


    /**
     * 결재 문서 생성 페이지
     */
    @GetMapping("/create")
    public String docCreate(HttpSession session, Model model, @RequestParam("formId") String formId) {
        User user = (User) session.getAttribute("user");
        DocCreate docCreate = approvalService.docCreate(user, formId);
        model.addAttribute("docCreate", docCreate);
        return "approval/doc-create";
    }


    /**
     * 결재 문서 생성
     */
    @PostMapping("/submit")
    public String createDoc(Model model, @ModelAttribute DocRequest request) {
        int docId = approvalService.submitDocument(request);
        DocDetail docDetail = approvalService.getDocDetail(docId);
        model.addAttribute("documents", docDetail);
        return "approval/doc-detail";
    }


    /**
     * 결재 문서 개별 조회
     */
    @GetMapping("/detail/{docId}")
    public String docDetail(Model model, @PathVariable int docId) {
        DocDetail docDetail = approvalService.getDocDetail(docId);
        model.addAttribute("documents", docDetail);
        return "approval/doc-detail";
    }



    /**
     * 결재 문서 수정
     */
    @PostMapping("/modify/{docNo}")
    public String modifyDoc(Model model, @PathVariable int docNo, @ModelAttribute DocRequest request) {
        approvalService.modifyDocument(docNo, request);
        List<DocResponse> documents = approvalService.getAllDocList();
        model.addAttribute("documents", documents);
        return "approval/main";
    }


    /**
     * 결재 문서 삭제
     */
    @GetMapping("/delete/{docNo}")
    @ResponseBody
    public ResponseEntity<?> deleteDoc(Model model, @PathVariable int docNo) {
        approvalService.deleteDocument(docNo);
//        List<DocResponse> documents = approvalService.getAllDocList();
//        model.addAttribute("documents", documents);
//        return "approval/main";
        return ResponseEntity.ok().build();
    }


    /**
     * 상신 문서 결재 처리
     */
    @ResponseBody
    @PatchMapping("/approveDoc/{lineNo}")
    public ResponseEntity<?> approveDoc(Model model, @PathVariable int lineNo, @RequestParam String status) {
        approvalService.approveDoc(lineNo, status);
        return ResponseEntity.ok().build();
    }


    /**
     * 회원 검색
     */
    @GetMapping("/userSearch")
    @ResponseBody
    public List<UserDto> searchUser(@RequestParam String keyword) {
        return userService.searchUser(keyword);
    }




}
