package com.resultant.task.service;

import com.resultant.task.entity.User;

public interface MailSenderService {

    void sendResetPassword(User user, String newPass);
}
