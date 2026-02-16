package com.myapp.service;

import com.myapp.dao.UserDAO;
import com.myapp.model.User;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    // Only keep if you actually have userDAO.create(user, passwordPlain)
    public int createUser(User user, String passwordPlain) {
        if (user == null) throw new IllegalArgumentException("user is null");
        if (passwordPlain == null || passwordPlain.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        // ✅ since User has no passwordHash, pass password separately
        return userDAO.create(user, passwordPlain); // you must add this method in UserDAO
    }

    public boolean updatePassword(int userId, String newPasswordPlain) {
        if (newPasswordPlain == null || newPasswordPlain.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        // ✅ store directly into password_hash column (same as your login does currently)
        return userDAO.updatePasswordHash(userId, newPasswordPlain);
    }
}
