package com.ht.qlktx.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${client.web.url}")
    private String clientWebUrl;

    public void sendForgotPasswordMail(String to, String token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        Context context = new Context();
        context.setVariable("forgotPasswordUrl", clientWebUrl + "/auth?type=resetPassword&token=" + token);

        String htmlContent = templateEngine.process("forgot-password", context);

        helper.setTo(to);
        helper.setSubject("Quản lí ký túc xá PTITHCM - Cài lại mật khẩu");
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }
}