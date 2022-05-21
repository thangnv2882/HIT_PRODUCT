package com.example.product.services;

public interface ISendMailService {
    String sendMailWithText(String title, String content, String to);
}
