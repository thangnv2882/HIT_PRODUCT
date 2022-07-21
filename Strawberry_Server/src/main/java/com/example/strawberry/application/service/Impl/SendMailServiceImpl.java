package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.EmailConstant;
import com.example.strawberry.application.service.ISendMailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import java.util.Properties;

@Service
public class SendMailServiceImpl implements ISendMailService {
    private final JavaMailSender javaMailSender;

    public SendMailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String sendMailWithText(String title, String content, String to) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setSubject(title);
            simpleMailMessage.setText(content);
            simpleMailMessage.setTo(to);

            javaMailSender.send(simpleMailMessage);

        }catch (Exception e) {
            return "Send failed";
        }
        return "Send successfully";
    }
}
