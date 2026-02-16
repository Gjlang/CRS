package com.myapp.service;

import com.myapp.dao.NotificationDAO;
import com.myapp.model.Notification;
import com.myapp.util.EmailUtil;

public class NotificationService {

    private final NotificationDAO notificationDAO = new NotificationDAO();

    /**
     * Save a notification in DB + send email.
     *
     * @param studentId  target student id (VARCHAR)
     * @param recipientEmail email destination (can be null if you don't want email)
     * @param subject email subject / notification title
     * @param body email body / notification message
     * @param createdByUserId users.user_id (who triggered this notification)
     */
    public void notifyStudent(String studentId, String recipientEmail, String subject, String body, int createdByUserId) {

        Notification n = new Notification();
        n.setStudentId(studentId);
        n.setTitle(subject);
        n.setMessage(body);
        n.setPriority("NORMAL");
        n.setStatus("NEW");
        n.setCreatedBy(createdByUserId);

        // 1) Save into DB
        notificationDAO.create(n);

        // 2) Send email if provided
        if (recipientEmail != null && !recipientEmail.isBlank()) {
            EmailUtil.send(recipientEmail, subject, body);
        }
    }

    /**
     * Notification linked to a recovery plan
     */
    public void notifyPlan(Integer planId, String recipientEmail, String subject, String body, int createdByUserId) {

        Notification n = new Notification();
        n.setPlanId(planId);
        n.setTitle(subject);
        n.setMessage(body);
        n.setPriority("NORMAL");
        n.setStatus("NEW");
        n.setCreatedBy(createdByUserId);

        notificationDAO.create(n);

        if (recipientEmail != null && !recipientEmail.isBlank()) {
            EmailUtil.send(recipientEmail, subject, body);
        }
    }
}
