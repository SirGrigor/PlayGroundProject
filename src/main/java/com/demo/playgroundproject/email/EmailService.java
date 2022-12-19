package com.demo.playgroundproject.email;

import com.demo.playgroundproject.utils.CustomErrorMessages;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("grigorjev.ilja@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error(
                    CustomErrorMessages.getMessage(CustomErrorMessages.Message.FAILED_TO_SEND_EMAIL_ERROR, e)
            );
            throw new IllegalStateException(CustomErrorMessages.getMessage(CustomErrorMessages.Message.FAILED_TO_SEND_EMAIL));
        }
    }
}
