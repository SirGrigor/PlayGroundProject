package com.demo.playgroundproject;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity(name = "Course")
@Table(name = "course")
public class Course {

    @Id
    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "department",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String department;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "course"
    )
    private List<Enrolment> enrolments = new ArrayList<>();

    public Course(Long id, String name, String department, List<Enrolment> enrolments) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.enrolments = enrolments;
    }
}
