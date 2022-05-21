package com.example.product.services.impl;

import com.example.product.services.ISendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailServiceImpl implements ISendMailService {
    @Autowired
    private JavaMailSender javaMailSender;
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
