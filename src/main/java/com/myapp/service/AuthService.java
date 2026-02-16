package com.myapp.service;

import com.myapp.dao.UserDAO;
import com.myapp.model.User;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String email, String passwordPlain) {
        if (email == null || email.isBlank() || passwordPlain == null) return null;

        // Your current DAO already checks:
        // WHERE email=? AND password_hash=? AND status='ACTIVE'
        return userDAO.login(email, passwordPlain);
    }
}
