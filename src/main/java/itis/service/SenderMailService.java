package itis.service;

import itis.model.User;
import itis.properties.MailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SenderMailService {
    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;

    @Async
    public void sendVerificationEmail(User user) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        String content = mailProperties.getContent();
        try {
            mimeMessageHelper.setFrom(mailProperties.getFrom(), mailProperties.getSender());
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject(mailProperties.getSubject());
            content = content.replace("$name", user.getName());
            content = content.replace("$url", mailProperties.getBaseUrl() + "/verification?code=" + user.getVerificationCode());

            mimeMessageHelper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Ошибка при формировании письма для пользователя с email: {}", user.getEmail(), e);
            throw new RuntimeException(e);
        }

    }
}
