package com.demo.playgroundproject.registration;

import com.demo.playgroundproject.registration.appuser.AppUser;
import com.demo.playgroundproject.registration.appuser.AppUserRole;
import com.demo.playgroundproject.registration.appuser.AppUserService;
import com.demo.playgroundproject.utils.CustomErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final AppUserService appUserService;

    private final EmailValidator emailValidator;

    @Autowired
    public RegistrationService(AppUserService appUserService, EmailValidator emailValidator) {
        this.appUserService = appUserService;
        this.emailValidator = emailValidator;
    }

    public String register(RegistrationRequest registrationRequest) {
        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException(new IllegalStateException(
                    CustomErrorMessages.getMessage(
                            CustomErrorMessages.Message.USER_NOT_FOUND_BY_USERNAME, registrationRequest.getEmail())));
        }
        return appUserService.signUpUser(
                new AppUser(
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getPassword(),
                        AppUserRole.USER
                )
        );
    }
}
