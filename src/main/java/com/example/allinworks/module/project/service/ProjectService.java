package com.example.allinworks.module.project.service;

import com.example.allinworks.module.project.domain.Project;
import com.example.allinworks.module.project.domain.ProjectDetail;
import com.example.allinworks.module.project.domain.ProjectStatus;
import com.example.allinworks.module.project.dto.*;
import com.example.allinworks.module.project.mapper.ProjectDetailMapper;
import com.example.allinworks.module.project.mapper.ProjectMapper;
import com.example.allinworks.module.project.repository.ProjectDetailRepository;
import com.example.allinworks.module.project.repository.ProjectRepository;
import com.example.allinworks.module.user.domain.Department;
import com.example.allinworks.module.user.domain.User;
import com.example.allinworks.module.user.repository.DepartmentRepository;
import com.example.allinworks.module.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectDetailRepository projectDetailRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    //칸반 보드의 내용이 변경되었을 경우 업데이트되었음을 브로드캐스팅
    public void broadcastKanbanUpdate(int projectNo) {
        // 클라이언트가 구독하는 채널로 메시지 전송
        messagingTemplate.convertAndSend("/topic/kanban/" + projectNo, "updated");
    }

    //부서번호로 프로젝트 조회
    public ProjectResponse getProjectByDept(String deptNo) {
        Optional<Project> project = projectRepository.findByDepartment_DeptNo(deptNo);

        if(project.isPresent()) { //해당 부서의 칸반 보드가 존재할 경우 존재하는 칸반 보드 반환
            return ProjectMapper.projectToResponse(project.get());
        } else { //해당 부서의 칸반 보드가 존재하지 않을 경우 생성
            //해당 유저의 부서 번호로 부서 조회
            Department department = departmentRepository.findById(deptNo).orElseThrow(EntityNotFoundException::new);

            //해당 유저의 부서를 참조하여 새 칸반 보드 생성
            Project newProject = Project.builder()
                    .projectNo(Integer.parseInt(deptNo))//해당 기본키를 부서번호와 동일하게 생성
                    .department(department)
                    .build();

            projectRepository.save(newProject);

            return ProjectMapper.projectToResponse(newProject);
        }
    }

    //카드 한 개 삽입
    @Transactional
    public void registerDetail(RequestCard card) {
        Optional<Project> optionalProject = projectRepository.findById(Integer.parseInt(card.getProjectNo()));
        Optional<User> optionalUser = userRepository.findById(card.getUserNo());

        //가져온 데이터를 통해 유저와 프로젝트 존재가 확인되면 save
        if(optionalProject.isPresent() && optionalUser.isPresent()) {
            ProjectDetail detail = ProjectDetailMapper.cardToDetail(card);
            long count = projectDetailRepository.countByStatus(detail.getStatus()) + 1;
            detail.setDetailOrder((int) count);
            detail.setProject(optionalProject.get());
            detail.setUser(optionalUser.get());

            //등록 후 브로드캐스팅 요청
            projectDetailRepository.save(detail);
            broadcastKanbanUpdate(optionalProject.get().getProjectNo());
        }
    }

    //카드 한 개 수정
    @Transactional
    public void updateDetail(RequestCard card, String detailNo) {
        Optional<Project> optionalProject = projectRepository.findById(Integer.parseInt(card.getProjectNo()));
        Optional<User> optionalUser = userRepository.findById(card.getUserNo());
        Optional<ProjectDetail> optionalDetail = projectDetailRepository.findById(Integer.parseInt(detailNo));

        if(optionalProject.isPresent() && optionalUser.isPresent() && optionalDetail.isPresent()) {
            ProjectDetail detail = optionalDetail.get();

            detail.setTitle(card.getTitle());
            detail.setPart(card.getPart());
            detail.setStartDate(LocalDate.parse(card.getStartDate()));
            detail.setEndDate(LocalDate.parse(card.getEndDate()));
            detail.setStatus(ProjectStatus.valueOf(card.getStatus().toUpperCase()));
            detail.setProject(optionalProject.get());
            detail.setUser(optionalUser.get());

            //갱신 후 브로드캐스팅 요청
            projectDetailRepository.save(detail);
            broadcastKanbanUpdate(optionalProject.get().getProjectNo());
        }
    }

    //카드 한 개 이동
    @Transactional
    public void moveCard(RequestCard card, String detailNo) {
        ProjectDetail detail = projectDetailRepository.findById(Integer.parseInt(detailNo))
                .orElseThrow(() -> new EntityNotFoundException("detail 조회 실패 " + detailNo));

        detail.setStatus(ProjectStatus.valueOf(card.getStatus().toUpperCase()));
        detail.setDetailOrder(card.getDetailOrder());

        //갱신 후 브로드캐스팅 요청
        projectDetailRepository.save(detail);
        broadcastKanbanUpdate(detail.getProject().getProjectNo());
    }

    public void deleteDetail(String detailNo) {
        ProjectDetail detail = projectDetailRepository.findById(Integer.parseInt(detailNo))
                .orElseThrow(EntityNotFoundException::new);
        projectDetailRepository.deleteById(Integer.parseInt(detailNo));

        broadcastKanbanUpdate(detail.getProject().getProjectNo());
    }

    //업데이트 후 보드 반환(후에 브로드캐스트 테스트 예정)
//    @Transactional
//    public ProjectResponse updateProject(String projectNo, ProjectRequest boards) {
//        List<KanbanColumn> columns = boards.getColumns();
//
//        //삭제 처리를 위한 card의 id 수집
//        Set<Integer> cardIds = columns.stream()
//                .flatMap(col -> col.getItem().stream())
//                .map(card -> Integer.parseInt(card.getId()))
//                .collect(Collectors.toSet());
//
//        //영속성 컨텍스트에서 project의 details 가져오기
//        Project entity = projectRepository.findById(Integer.parseInt(projectNo))
//                .orElseThrow(() -> new EntityNotFoundException("No project found for id " + projectNo));
//        List<ProjectDetail> details = entity.getDetails();
//
//        //삭제 대상을 별도 리스트에 모아서 삭제
//        List<ProjectDetail> toDelete = new ArrayList<>();
//        for (ProjectDetail detail : details) {
//            if (!cardIds.contains(detail.getDetailNo())) {
//                toDelete.add(detail);
//            }
//        }
//
//        for (ProjectDetail detail : toDelete) {
//            projectDetailRepository.delete(detail);
//            System.out.println("카드 하나 삭제됨");
//        }
//
//        for (KanbanColumn column : columns) {
//            ProjectStatus status = ProjectStatus.valueOf(column.getId().toUpperCase());//카드의 소속 column(status) 반환
//            List<ResponseCard> cards = column.getItem();
//            for (int i = 0; i < cards.size(); i++) {
//                //영속성 컨텍스트에서 기존 detail 로드
//                ResponseCard card = cards.get(i);
//                Optional<ProjectDetail> detail = projectDetailRepository.findById(Integer.parseInt(card.getId()));
//
//                //존재할 경우 필드 업데이트
//                if (detail.isPresent()) {
//                    ProjectDetail result = detail.get();
//
//                    result.setStatus(status);
//                    result.setTitle(card.getTitle());
//                    result.setStartDate(LocalDate.parse(card.getStartDate()));
//                    result.setEndDate(LocalDate.parse(card.getEndDate()));
//                    result.setPart(card.getPart());
//                    result.setDetailOrder(card.getDetailOrder());
//
//                    //변경 사항 저장
//                    projectDetailRepository.save(result);
//                }
//            }
//        }
//
//        //projectDetail이 갱신된 project 호출
//        Project project = projectRepository.findById(Integer.parseInt(projectNo))
//                .orElseThrow(() ->
//                        new EntityNotFoundException("No project found for id " + projectNo));
//
//        return ProjectMapper.projectToResponse(project);
//    }
}
