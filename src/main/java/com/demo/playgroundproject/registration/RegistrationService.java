package com.demo.playgroundproject.registration;

import com.demo.playgroundproject.email.EmailSender;
import com.demo.playgroundproject.registration.appuser.AppUser;
import com.demo.playgroundproject.registration.appuser.AppUserRole;
import com.demo.playgroundproject.registration.appuser.AppUserService;
import com.demo.playgroundproject.registration.token.ConfirmationToken;
import com.demo.playgroundproject.registration.token.ConfirmationTokenService;
import com.demo.playgroundproject.student.model.Student;
import com.demo.playgroundproject.student.service.StudentService;
import com.demo.playgroundproject.utils.CustomErrorMessages;
import com.demo.playgroundproject.utils.EmailBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final StudentService studentService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

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
        String token = appUserService.signUpUser(
                new AppUser(
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getPassword(),
                        AppUserRole.USER,
                        student
                )
        );
        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(
                registrationRequest.getEmail(),
                EmailBuilder.buildRegistrationEmail(
                        registrationRequest.getFirstName(),
                        link
                ));
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }
}
