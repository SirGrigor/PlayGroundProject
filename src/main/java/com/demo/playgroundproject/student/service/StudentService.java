package com.demo.playgroundproject.student.service;

import com.demo.playgroundproject.student.model.Student;
import com.demo.playgroundproject.student.repository.StudentRepository;
import com.demo.playgroundproject.utils.CustomErrorMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;


    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException(
                    CustomErrorMessages.getMessage(
                            CustomErrorMessages.Message.USER_NOT_FOUND_BY_USERNAME, student.getEmail()));
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalStateException(
                    CustomErrorMessages.getMessage(
                            CustomErrorMessages.Message.USER_NOT_FOUND_BY_ID, studentId));
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId,
                              String firstName,
                              String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        CustomErrorMessages.getMessage(
                                CustomErrorMessages.Message.USER_NOT_FOUND_BY_ID, studentId)));

        if (firstName != null &&
                firstName.length() > 0 &&
                !Objects.equals(student.getFirstName(), firstName)) {
            student.setFirstName(firstName);
        }

        if (email != null &&
                email.length() > 0 &&
                !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException(CustomErrorMessages.getMessage(CustomErrorMessages.Message.USER_NOT_FOUND_BY_USERNAME, email));
            }
            student.setEmail(email);
        }
    }
}
