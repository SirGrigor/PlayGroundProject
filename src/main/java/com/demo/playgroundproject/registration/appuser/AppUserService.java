package com.demo.playgroundproject.registration.appuser;

import com.demo.playgroundproject.registration.token.ConfirmationToken;
import com.demo.playgroundproject.registration.token.ConfirmationTokenService;
import com.demo.playgroundproject.utils.CustomErrorMessages;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        CustomErrorMessages.getMessage(CustomErrorMessages.Message.USER_NOT_FOUND_BY_USERNAME, email)));
    }

    @Transactional
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

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
