package com.example.allinworks.module.project.domain;

import com.example.allinworks.module.user.domain.Department;
import com.example.allinworks.module.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PROJECT")
@Entity
public class Project {
    @Id
    @Column(name = "PROJECT_NO", nullable = false, length = 10)
    private int projectNo;

    @ManyToOne
    @JoinColumn(name = "DEPT_NO")
    private Department department;

    //가져온 detail의 detailOrder에 따라 정렬
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("detailOrder")
    @Builder.Default
    private List<ProjectDetail> details = new ArrayList<>();

    public void addDetail(ProjectDetail detail) {
        details.add(detail);
        detail.setProject(this);
    }
}

