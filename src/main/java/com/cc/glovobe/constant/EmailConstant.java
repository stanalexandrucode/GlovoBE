package com.cc.glovobe.constant;

import org.springframework.beans.factory.annotation.Value;

public class EmailConstant {
    @Value("${spring.mail.username}")
    private static String emailSender;
    @Value("${spring.mail.password}")
    private static String password;

    public static final String SIMPLE_MAIL_TRANSFER_PROTOCOL = "smtps";
    @Value("${spring.mail.username}")
    public static final String USERNAME = "teamteam.delivery@gmail.com";
    public static final String PASSWORD = "codeTeam@20";
    public static final String FROM_EMAIL = "teamteam.delivery@gmail.com";
    public static final String CC_EMAIL = "";
    public static final String EMAIL_SUBJECT = "Team Team Delivery: Activation Link";
    public static final String GMAIL_SMTP_SERVER = "smtp.gmail.com";
    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_AUTH = "mail.smtp.auth";
    public static final String SMTP_PORT = "mail.smtp.port";
    public static final int DEFAULT_PORT = 587;
    public static final String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
}
