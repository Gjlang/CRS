package com.myapp.service;

import com.myapp.dao.PasswordResetDAO;
import com.myapp.dao.UserDAO;
import com.myapp.model.User;
import com.myapp.util.EmailUtil;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class PasswordResetService {

    private final PasswordResetDAO resetDAO = new PasswordResetDAO();
    private final UserDAO userDAO = new UserDAO();
    private final UserService userService = new UserService();

    public void requestReset(String email, String appResetLinkBase) {
        User user = userDAO.findByEmail(email); // you must add findByEmail in UserDAO
        if (user == null) {
            // don't reveal if email exists
            return;
        }

        String token = generateToken();
        Instant expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);

        // IMPORTANT: your User primary key is user_id, and your model uses getUserId()
        resetDAO.createToken(user.getUserId(), token, expiresAt);

        String link = appResetLinkBase + "?token=" + token;
        String subject = "CRS Password Reset";
        String body = "Click this link to reset your password (valid for 30 minutes):\n" + link;

        EmailUtil.send(email, subject, body);
    }

    public boolean resetPassword(String token, String newPasswordPlain) {
        Integer userId = resetDAO.findUserIdByValidToken(token);
        if (userId == null) return false;

        userService.updatePassword(userId, newPasswordPlain);
        resetDAO.deleteToken(token);
        return true;
    }

    private String generateToken() {
        byte[] buf = new byte[32];
        new SecureRandom().nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }
}
