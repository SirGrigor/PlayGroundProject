package com.demo.playgroundproject;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity(name = "Enrolment")
@Table(name = "enrolment")
public class Enrolment {

    @EmbeddedId
    private EnrolmentId id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(
            name = "student_id",
            foreignKey = @ForeignKey(
                    name = "enrolment_student_id_fk"
            )
    )
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(
            name = "course_id",
            foreignKey = @ForeignKey(
                    name = "enrolment_course_id_fk"
            )
    )
    private Course course;

    @Column(
            name = "created_at",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private LocalDateTime createdAt;

    public Enrolment(EnrolmentId id, Student student, Course course, LocalDateTime createdAt) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.createdAt = createdAt;
    }
}
