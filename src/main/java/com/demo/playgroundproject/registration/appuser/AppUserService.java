package com.demo.playgroundproject.registration.appuser;

import com.demo.playgroundproject.utils.CustomErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        CustomErrorMessages.getMessage(CustomErrorMessages.Message.USER_NOT_FOUND_BY_USERNAME, email)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();
        if (userExists) {
            throw new IllegalStateException(
                    CustomErrorMessages.getMessage(
                            CustomErrorMessages.Message.USER_ALREADY_EXISTS, appUser.getEmail()));
        }
        String encodePassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodePassword);
        appUserRepository.save(appUser);
        return "it works";
    }
}
