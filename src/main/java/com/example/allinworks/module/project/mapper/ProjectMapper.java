package com.example.allinworks.module.project.mapper;

import com.example.allinworks.module.project.domain.Project;
import com.example.allinworks.module.project.domain.ProjectDetail;
import com.example.allinworks.module.project.domain.ProjectStatus;
import com.example.allinworks.module.project.dto.ResponseCard;
import com.example.allinworks.module.project.dto.KanbanColumn;
import com.example.allinworks.module.project.dto.ProjectResponse;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectMapper {
    public static ProjectResponse projectToResponse(Project project) {
        Map<ProjectStatus, List<ProjectDetail>> grouped = project.getDetails().stream()
                .collect(Collectors.groupingBy(
                        ProjectDetail::getStatus,//status 별로 projectDetail grouping
                        //grouping 후 detailOrder대로 정렬
                        Collectors.collectingAndThen(
                                Collectors.toList(), list -> {
                                    list.sort(Comparator.comparingInt(ProjectDetail::getDetailOrder));
                                    return list;
                                }
                        )
                ));
        List<KanbanColumn> columns = Arrays.asList(
                buildColumn("todo", "To Do", ProjectStatus.TODO, grouped),
                buildColumn("in_progress", "In Progress", ProjectStatus.IN_PROGRESS, grouped),
                buildColumn("in_review", "In_Review", ProjectStatus.IN_REVIEW, grouped),
                buildColumn("done", "Done", ProjectStatus.DONE, grouped)
        );

        ProjectResponse response = new ProjectResponse();
        response.setProjectNo(project.getProjectNo());
        response.setColumns(columns);

        return response;
    }

    //status를 kanban board에서 사용하는 컬럼의 클래스명으로 치환
    private static final Map<ProjectStatus, String> STATUS_TO_CLASS = Map.of(
            ProjectStatus.TODO, "todo-header",
            ProjectStatus.IN_PROGRESS, "progress-header",
            ProjectStatus.IN_REVIEW, "review-header",
            ProjectStatus.DONE, "done-header"
    );

    private static KanbanColumn buildColumn(
            String id, String title, ProjectStatus status, Map<ProjectStatus, List<ProjectDetail>> grouped) {
        List<ResponseCard> items = grouped.getOrDefault(status, List.of()).stream()
                .map(ProjectDetailMapper::detailToCard)
                .toList();

        KanbanColumn column = new KanbanColumn();
        column.setId(id);
        column.setTitle(title);
        column.setClassName(STATUS_TO_CLASS.get(status));
        column.setItem(items);

        return column;
    }
}
