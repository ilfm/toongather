package com.toongather.toongather.domain.member.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender javaMailSender;
  private final org.thymeleaf.spring6.SpringTemplateEngine templateEngine;

  public void sendEmail(String type, String code, String email) {

    String subject = type.equals("resetpwd") ? "ToonGather 임시 비밀번호 발급" : "ToonGather 회원가입 인증";

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");


    try {
      mimeMessageHelper.setSubject(subject);
      mimeMessageHelper.setText(setContext(code, type), true);
      mimeMessageHelper.setTo(email);
      javaMailSender.send(mimeMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private String setContext(String code, String type) {
    Context context = new Context();
    context.setVariable("code", code);
    return templateEngine.process(type, context);
  }



}
