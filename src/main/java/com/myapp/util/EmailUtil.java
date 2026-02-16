package com.myapp.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {

    // =========================
    // SMTP Configuration
    // =========================
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;

    // ⚠️ Replace with your Gmail + App Password
    private static final String SMTP_USER = "yourgmail@gmail.com";
    private static final String SMTP_PASS = "your_app_password";

    // =========================
    // Send Email
    // =========================
    public static void send(String to, String subject, String body) {

        try {
            Session session = Session.getInstance(buildProps(), new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully to " + to);

        } catch (Exception e) {
            // Do NOT crash system if email fails
            e.printStackTrace();
        }
    }

    // =========================
    // Mail Properties
    // =========================
    private static Properties buildProps() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        return props;
    }
}
