package com.demo.playgroundproject.student.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EnrolmentId implements Serializable {

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "course_id")
    private Long courseId;

    @Override
    public String toString() {
        return "EnrolmentId{" +
                "studentId=" + studentId +
                ", courseId=" + courseId +
                '}';
    }
}
