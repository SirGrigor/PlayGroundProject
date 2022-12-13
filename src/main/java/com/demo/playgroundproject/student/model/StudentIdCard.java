package com.demo.playgroundproject.student.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "StudentIdCard")
@Table(
        name = "student_id_card",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "student_id_card_number_unique",
                        columnNames = "card_number"
                )
        }
)
public class StudentIdCard {

    @Id
    @SequenceGenerator(
            name = "student_card_id_sequence",
            sequenceName = "student_card_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_card_id_sequence"
    )
    @Column(
            name = "student_card_id",
            updatable = false
    )
    private Long studentCardId;

    @Column(
            name = "card_number",
            nullable = false,
            length = 15
    )
    private String cardNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "student_id",
            referencedColumnName = "student_id",
            foreignKey = @ForeignKey(
                    name = "student_id_fk"
            )
    )
    private Student student;

    @Override
    public String toString() {
        return "StudentIdCard{" +
                "studentCardId=" + studentCardId +
                ", cardNumber='" + cardNumber + '\'' +
                ", student=" + student +
                '}';
    }
}
