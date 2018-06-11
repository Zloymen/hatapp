package com.resultant.task.service.impl;

import com.resultant.task.entity.User;
import com.resultant.task.error.AppException;
import com.resultant.task.service.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@PropertySource("classpath:appenv.properties")
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {

    @Value("${template.email_address}")
    private String address;

    @Value("${template.email_personal}")
    private String personal;


    @Autowired
    private JavaMailSender sender;

    @Override
    public void sendResetPassword(User user, String newPass) {
        MimeMessage message = sender.createMimeMessage();
        try {

            message.setFrom(new InternetAddress(address, personal));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getUsername()));

            message.setSubject("Reset password");
            //текст
            message.setContent("<!DOCTYPE html>\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "        <head>\n" +
                    "                <meta http-equiv=\"Content-Type\" Content=\"text/html\"; Charset=UTF-8\">\n" +
                    "        </head>\n" +
                    "        <body>\n" +
                    "                <b>Your new password: </b>" + newPass +
                    "   Change after login." +
                    "        </body>\n" +
                    "</html>", "text/html");


            sender.send(message);
        }catch (Exception e) {
            log.debug("send email exception:", e);
            throw new AppException("send email exception");
        }

    }
}
