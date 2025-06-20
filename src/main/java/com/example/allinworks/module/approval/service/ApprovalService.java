package com.example.allinworks.module.approval.service;

import com.example.allinworks.common.Common;
import com.example.allinworks.module.approval.domain.ApprovalDoc;
import com.example.allinworks.module.approval.domain.ApprovalDocForm;
import com.example.allinworks.module.approval.domain.ApprovalLine;
import com.example.allinworks.module.approval.dto.*;
import com.example.allinworks.module.approval.repository.ApprovalLineRepository;
import com.example.allinworks.module.approval.repository.ApprovalRepository;
import com.example.allinworks.module.approval.repository.DocFormRepository;
import com.example.allinworks.module.user.domain.Department;
import com.example.allinworks.module.user.domain.Position;
import com.example.allinworks.module.user.domain.User;
import com.example.allinworks.module.user.repository.DepartmentRepository;
import com.example.allinworks.module.user.repository.PositionRepository;
import com.example.allinworks.module.user.repository.UserRepository;
import com.example.allinworks.module.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ApprovalService {
    private final ApprovalRepository approvalRepository;
    private final DocFormRepository docFormRepository;
    private final ApprovalLineRepository approvalLineRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;



    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


    public List<DocResponse> getAllDocList() {
        List<ApprovalDoc> docList = approvalRepository.findByDelYn("N");
        docList.sort(Comparator.comparing(ApprovalDoc::getCreatedAt).reversed());

        List<String> userNos = docList.stream()
                .map(ApprovalDoc::getUserNo)
                .distinct()
                .collect(Collectors.toList());

        Map<String, String> userNameMap = userRepository.findByUserNoIn(userNos).stream()
                .collect(Collectors.toMap(User::getUserNo, User::getUserName));

        List<DocResponse> result = new ArrayList<>();
        for(ApprovalDoc doc : docList) {
            result.add(convertToDocResponse(doc, userNameMap));
        }
        return result;
    }



    public List<DocResponse> getUserDocList(String userId, String status) {
        List<ApprovalDoc> docList = approvalRepository.findByUserNoAndStatusAndDelYn(userId, status, "N");
        docList.sort(Comparator.comparing(ApprovalDoc::getCreatedAt).reversed());

        List<String> userNos = docList.stream()
                .map(ApprovalDoc::getUserNo)
                .distinct()
                .collect(Collectors.toList());

        Map<String, String> userNameMap = userRepository.findByUserNoIn(userNos).stream()
                .collect(Collectors.toMap(User::getUserNo, User::getUserName));

        List<DocResponse> result = new ArrayList<>();
        for(ApprovalDoc doc : docList) {
            result.add(convertToDocResponse(doc, userNameMap));
        }
        return result;
    }


    private DocResponse convertToDocResponse(ApprovalDoc doc, Map<String, String> userNameMap) {
        DocResponse docResponse = new DocResponse();
        docResponse.setId(doc.getDocumentNo());
        docResponse.setTitle(doc.getTitle());
        docResponse.setUserId(doc.getUserNo());
        docResponse.setCreateDate(Common.dateTimeFormat2(doc.getCreatedAt()));
        docResponse.setUserName(userNameMap.get(doc.getUserNo()));
        docResponse.setStatus(getDocStatus(doc.getStatus()));
        return docResponse;
    }


    private DocResponse convertToDocResponse2(ApprovalDoc doc) {
        DocResponse docResponse = new DocResponse();
        docResponse.setId(doc.getDocumentNo());
        docResponse.setTitle(doc.getTitle());
        docResponse.setUserId(doc.getUserNo());
        docResponse.setCreateDate(Common.dateTimeFormat2(doc.getCreatedAt()));
        docResponse.setUserName(getUserName(doc.getUserNo()));
        docResponse.setStatus(getDocStatus(doc.getStatus()));
        return docResponse;
    }


    public DocDetail getDocDetail(Integer docNo) {
        log.info("1 : {}", LocalDateTime.now().format(FORMATTER));
        ApprovalDoc doc = approvalRepository.findByDocumentNoAndDelYn(docNo, "N")
                .orElseThrow(() -> new RuntimeException("Document not found"));

        log.info("2 : {}", LocalDateTime.now().format(FORMATTER));
        Optional<ApprovalLine> approveUser = approvalLineRepository.findByApprovalNoAndApprovalOrder(docNo, "1");
        Optional<ApprovalLine> confirmUser = approvalLineRepository.findByApprovalNoAndApprovalOrder(docNo, "2");

        log.info("3 : {}", LocalDateTime.now().format(FORMATTER));

        User user = userRepository.findWithDepartmentByUserNo(doc.getUserNo())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String deptName = user.getDepartment() != null ? user.getDepartment().getDeptName() : null;

        String positionName = null;
        if (user.getPosition() != null) {
            Position position = positionRepository.findByPositionNo(user.getPosition());
            if (position != null) {
                positionName = position.getPositionName();
            }
        }

        log.info("4 : {}", LocalDateTime.now().format(FORMATTER));

        DocDetail docDetail = new DocDetail();
        docDetail.setId(doc.getDocumentNo());
        docDetail.setUserName(user.getUserName());
        docDetail.setUserId(doc.getUserNo());
        docDetail.setDept(deptName);
        docDetail.setPosition(positionName);
        docDetail.setTitle(doc.getTitle());
        docDetail.setContent(doc.getContent());
        docDetail.setCreateDate(Common.dateTimeFormat2(doc.getCreatedAt()));

        log.info("5 : {}", LocalDateTime.now().format(FORMATTER));

        Set<String> userNos = new HashSet<>();
        approveUser.ifPresent(u -> userNos.add(u.getUserNo()));
        confirmUser.ifPresent(u -> userNos.add(u.getUserNo()));

        Map<String, User> userMap = userRepository.findAllById(userNos)
                .stream()
                .collect(Collectors.toMap(User::getUserNo, Function.identity()));

        Set<String> positionNos = userMap.values().stream()
                .map(User::getPosition)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<String, Position> positionMap = positionRepository.findAllByPositionNoIn(positionNos)
                .stream()
                .collect(Collectors.toMap(Position::getPositionNo, Function.identity()));

        if (approveUser.isPresent()) {
            ApprovalLine line = approveUser.get();
            User approveUserObj = userMap.get(line.getUserNo());
            Position approveUserPosition = (approveUserObj != null) ? positionMap.get(approveUserObj.getPosition()) : null;

            docDetail.setApprovalNo(line.getLineNo());
            docDetail.setApproveUserNo(line.getUserNo());
            docDetail.setApproveUserName(approveUserObj != null ? approveUserObj.getUserName() : null);
            docDetail.setApproveStatus(getLineStatus(line.getStatus()));
            docDetail.setApproveUserPosition(approveUserPosition != null ? approveUserPosition.getPositionName() : null);
            if (!Objects.equals(line.getStatus(), "100")) {
                docDetail.setApproveDate(Common.dateTimeFormat2(line.getApprovedAt()));
            }
        }

        if (confirmUser.isPresent()) {
            ApprovalLine line = confirmUser.get();
            User confirmUserObj = userMap.get(line.getUserNo());
            Position confirmUserPosition = (confirmUserObj != null) ? positionMap.get(confirmUserObj.getPosition()) : null;

            docDetail.setConfirmNo(line.getLineNo());
            docDetail.setConfirmUserNo(line.getUserNo());
            docDetail.setConfirmUserName(confirmUserObj != null ? confirmUserObj.getUserName() : null);
            docDetail.setConfirmStatus(getLineStatus(line.getStatus()));
            docDetail.setConfirmUserPosition(confirmUserPosition != null ? confirmUserPosition.getPositionName() : null);
            if (!Objects.equals(line.getStatus(), "100")) {
                docDetail.setConfirmDate(Common.dateTimeFormat2(line.getApprovedAt()));
            }
        }
            log.info("7 : {}", LocalDateTime.now().format(FORMATTER));
            return docDetail;
        }



    public List<DocResponse> getConfirmDocList(String userNo) {
//        List<ApprovalLine> lineList = approvalLineRepository.findByUserNoAndStatus(userNo, "100");
//        lineList.sort(Comparator.comparing(ApprovalLine::getCreatedAt).reversed());
//        List<String> userNos = lineList.stream()
//                .map(ApprovalLine::getUserNo)
//                .distinct()
//                .collect(Collectors.toList());
//
//        Map<String, String> userNameMap = userRepository.findByUserNoIn(userNos).stream()
//                .collect(Collectors.toMap(User::getUserNo, User::getUserName));
//
//        List<DocResponse> result = new ArrayList<>();
//        for(ApprovalLine line : lineList) {
//            Optional<ApprovalDoc> docCheck = approvalRepository.findByDocumentNoAndDelYn(line.getApprovalNo(), "N");
//            docCheck.ifPresent(approvalDoc -> result.add(convertToDocResponse(approvalDoc, userNameMap)));
//        }
//
//        log.info(result.toString());
//        return result;
        List<ApprovalLine> lineList = approvalLineRepository.findByUserNoAndStatus(userNo, "100");
        lineList.sort(Comparator.comparing(ApprovalLine::getCreatedAt).reversed());
        List<DocResponse> result = new ArrayList<>();
        for(ApprovalLine line : lineList) {
            Optional<ApprovalDoc> docCheck = approvalRepository.findByDocumentNoAndDelYn(line.getApprovalNo(), "N");
            docCheck.ifPresent(approvalDoc -> result.add(convertToDocResponse2(approvalDoc)));
        }
        return result;
    }


    @Transactional
    public int submitDocument(DocRequest request) {
        int docNo = approvalRepository.findMaxDocNo() + 1;

        log.info("docNo:{}", docNo);
        log.info("title:{}", request.getTitle());
        ApprovalDoc approvalDoc = ApprovalDoc.builder()
                .documentNo(docNo)
                .title(request.getTitle())
                .content(request.getContent())
                .userNo(request.getUserId())
                .createdAt(LocalDateTime.now())
                .status("10")
                .delYn("N")
                .build();
        approvalRepository.save(approvalDoc);

        //TODO 결재라인 지정
        if(request.getApproveUserId() != null) {
            ApprovalLine approvalLine = ApprovalLine.builder()
                    .lineNo(Common.getNumberInt())
                    .approvalNo(docNo)
                    .approvalOrder("1")
                    .userNo(request.getApproveUserId())
                    .createdAt(LocalDateTime.now())
                    .status("100")
                    .build();
            approvalLineRepository.save(approvalLine);
        }

        if(request.getConfirmUserId() != null) {
            ApprovalLine approvalLine = ApprovalLine.builder()
                    .lineNo(Common.getNumberInt())
                    .approvalNo(docNo)
                    .approvalOrder("2")
                    .createdAt(LocalDateTime.now())
                    .userNo(request.getConfirmUserId())
                    .status("100")
                    .build();
            approvalLineRepository.save(approvalLine);
        }
        return docNo;
    }

    public void deleteDocument(int docNo) {
        Optional<ApprovalDoc> docResponse = approvalRepository.findByDocumentNoAndDelYn(docNo, "N");
        if(docResponse.isPresent()) {
            ApprovalDoc approvalDoc = docResponse.get();
            approvalDoc.updateDelYn();
            approvalRepository.save(approvalDoc);
        }
    }

    public void modifyDocument(int docNo, DocRequest request) {
        Optional<ApprovalDoc> docResponse = approvalRepository.findByDocumentNoAndDelYn(docNo, "N");
        if(docResponse.isEmpty()) {
            throw new RuntimeException();
        }

        ApprovalDoc approvalDoc = docResponse.get();
        approvalDoc.modifyDocument();
        approvalRepository.save(approvalDoc);
    }

    public Map<String, List<DocForm>> getFormList() {
        List<ApprovalDocForm> formList = docFormRepository.findByDelYn("N");

        List<DocForm> docForms = formList.stream()
                .map(form -> {
                    DocForm docForm = new DocForm();
                    docForm.setFormId(form.getFormId());
                    docForm.setFormName(form.getFormName());
                    docForm.setFormGroup(FORM_GROUP_NAME_MAP.get(form.getFormGroup()));
                    return docForm;
                })
                .toList();

        return docForms.stream()
                .collect(Collectors.groupingBy(DocForm::getFormGroup));

    }

    private static final Map<String, String> FORM_GROUP_NAME_MAP = Map.of(
            "1", "공통/행정",
            "2", "재무",
            "3", "인사",
            "4", "영업"
    );


    public DocCreate docCreate(User user, String formId) {
        Optional<ApprovalDocForm> formCheck = docFormRepository.findByFormIdAndDelYn(formId, "N");
        if(formCheck.isEmpty()) {
            throw new RuntimeException();
        }

        Department department = departmentRepository.findByDeptNo(user.getDepartment().getDeptNo());
        String deptNm = department.getDeptName();

        log.info("deptNm:{}", deptNm);

        String formName = formCheck.get().getFormName();
        DocCreate docCreate = new DocCreate();
        docCreate.setTitle(formName);
        docCreate.setUserName(user.getUserName());
        docCreate.setUserDept(deptNm);
        docCreate.setUserNo(user.getUserNo());
        docCreate.setDate(Common.dateTimeFormat());

        return docCreate;
    }


    public String getUserName(String userNo) {
        Optional<User> userCheck = userRepository.findById(userNo);
        if(userCheck.isEmpty()){
            throw new RuntimeException();
        }

        String userName = userCheck.get().getUserName();
        if(userName.isEmpty()){
            return "";
        }
        return userName;
    }


    public String getDocStatus(String status) {
        return switch (status) {
            case "20" -> "반려";
            case "30" -> "완료";
            default -> "결재중";
        };
    }


    public String getLineStatus(String status) {
        return switch (status) {
            case "200" -> "반려";
            case "300" -> "승인";
            default -> "결재중";
        };
    }


    public String getDeptName(String userNo) {
        Optional<User> userCheck = userRepository.findById(userNo);
        if(userCheck.isEmpty()){
            return null;
        }
        String deptNo = userCheck.get().getDepartment().getDeptNo();
        Optional<Department> deptCheck = departmentRepository.findById(deptNo);
        return deptCheck.map(Department::getDeptName).orElse(null);
    }


    public String getPositionName(String userNo) {
        Optional<User> userCheck = userRepository.findById(userNo);
        if(userCheck.isEmpty()){
            return null;
        }
        String positionNo = userCheck.get().getPosition();
        Position position = positionRepository.findByPositionNo(positionNo);
        if(position == null){
            return null;
        }
        return position.getPositionName();
    }


    public String getPosition(String userNo) {
        Optional<User> userCheck = userRepository.findById(userNo);
        return userCheck.map(User::getPosition).orElse(null);
    }


    public void approveDoc(int lineNo, String status) {
        Optional<ApprovalLine> check = approvalLineRepository.findByLineNo(lineNo);
        if(check.isPresent()) {
            ApprovalLine approvalLine = check.get();
            approvalLine.updateStatus(status);
            approvalLineRepository.save(approvalLine);
        }
    }
}
