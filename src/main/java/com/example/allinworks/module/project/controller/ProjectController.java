package com.example.allinworks.module.project.controller;

import com.example.allinworks.module.project.dto.*;
import com.example.allinworks.module.project.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/project/index")
    public String project() {
        return "project/index";
    }

    //새 작업 추가 요청
    @PostMapping("/projectDetails")
    public ResponseEntity<Void> addDetail(@RequestBody RequestCard card) {
        projectService.registerDetail(card);

        return ResponseEntity.ok().build();
    }

    //카드 수정 요청
    @PutMapping("/projectDetail/{detailNo}")
    public ResponseEntity<Void> updateDetail(@RequestBody RequestCard card, @PathVariable String detailNo) {
        projectService.updateDetail(card, detailNo);

        return ResponseEntity.ok().build();
    }

    //카드 이동 처리 요청
    @PatchMapping("/projectDetail/{detailNo}/moved")
    public ResponseEntity<Void> moveCard(@RequestBody RequestCard card, @PathVariable String detailNo) {
        System.out.println("카드 요청 시 정보:" + card.getDetailOrder());
        System.out.println("카드 요청 시 정보:" + card.getStatus());

        projectService.moveCard(card, detailNo);

        return ResponseEntity.ok().build();
    }

    //해당 부서의 프로젝트 가져오기
    @GetMapping("/project/{deptNo}")
    @ResponseBody
    public ProjectResponse getProject(@PathVariable String deptNo) {
        ProjectResponse boards = projectService.getProjectByDept(deptNo);

        System.out.println("불러온 보드의 첫번째 카드의 제목: "  + boards.getColumns().get(0).getTitle());

        return boards;
    }

    //카드 내용 수정
//    @PutMapping("/project/{projectNo}")
//    public ResponseEntity<?> updateProject(@PathVariable String projectNo, @RequestBody ProjectRequest boards) {
//        System.out.println(boards);
//
//        ProjectResponse board = projectService.updateProject(projectNo, boards);
//
//        return ResponseEntity.ok(board);
//    }

    //카드 삭제
    @DeleteMapping("/projectDetail/{detailNo}")
    public ResponseEntity<Void> deleteProject(@PathVariable String detailNo) {
        projectService.deleteDetail(detailNo);

        return ResponseEntity.ok().build();
    }
}
