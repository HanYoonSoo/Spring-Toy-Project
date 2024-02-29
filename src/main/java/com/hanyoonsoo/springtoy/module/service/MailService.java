package com.hanyoonsoo.springtoy.module.service;

import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail,
                          String title,
                          String text){
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try{
            emailSender.send(emailForm);
        } catch (RuntimeException e){
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);

            throw new BusinessLogicException(ErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
