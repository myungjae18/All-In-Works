
package com.example.allinworks.module.user.domain;

import jakarta.persistence.*;
        import lombok.*;


@Getter @Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DEPARTMENT")

public class Department {
    @Id
    @Column(name = "DEPT_NO", nullable = false, length = 10)
    private String deptNo;

    @Column(name = "DEPT_NAME", nullable = false, length = 50)
    private String deptName;
}
