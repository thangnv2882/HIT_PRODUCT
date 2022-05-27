package com.example.strawberry.application.service;

public interface ISendMailService {
    String sendMailWithText(String title, String content, String to);
}
