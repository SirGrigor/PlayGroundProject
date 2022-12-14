package com.demo.playgroundproject.registration;

import com.demo.playgroundproject.registration.appuser.AppUser;
import com.demo.playgroundproject.registration.appuser.AppUserRole;
import com.demo.playgroundproject.registration.appuser.AppUserService;
import com.demo.playgroundproject.student.model.Student;
import com.demo.playgroundproject.student.service.StudentService;
import com.demo.playgroundproject.utils.CustomErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final AppUserService appUserService;
    private final StudentService studentService;
    private final EmailValidator emailValidator;

    @Autowired
    public RegistrationService(AppUserService appUserService, StudentService studentService, EmailValidator emailValidator) {
        this.appUserService = appUserService;
        this.studentService = studentService;
        this.emailValidator = emailValidator;
    }

    public String register(RegistrationRequest registrationRequest) {
        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException(new IllegalStateException(
                    CustomErrorMessages.getMessage(
                            CustomErrorMessages.Message.USER_NOT_FOUND_BY_USERNAME, registrationRequest.getEmail())));
        }

        Student student = new Student(
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail()
        );
        studentService.addNewStudent(student);
        return appUserService.signUpUser(
                new AppUser(
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getPassword(),
                        AppUserRole.USER,
                        student
                )
        );
    }
}
