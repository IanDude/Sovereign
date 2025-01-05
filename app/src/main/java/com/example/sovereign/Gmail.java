package com.example.sovereign;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Gmail {

    final String emailPort = "587";
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";

    String fromEmail;
    String fromPassword;
    List<String> toEmailList;
    String emailSubject;
    String emailBody;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    protected Gmail(String fromEmail, String fromPassword, List<String> toEmailList, String emailSubject, String emailBody) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");
    }


    protected void createEmailMessage() throws MessagingException, UnsupportedEncodingException {
        mailSession = Session.getDefaultInstance(emailProperties,null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail,fromEmail));
        for (String toEmail : toEmailList){
            Log.i("GMail","toEmail:"+toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));
        }
        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody,"text/html");
        Log.i("GMail","Email Message created.");

    }
    protected void sendEmail() {
        try {
            if (emailMessage == null) {
                Log.e("GMail", "Email message is null. Ensure createEmailMessage() is called before sendEmail().");
                return;
            }

            Log.i("GMail", "Connecting to SMTP server...");
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(emailHost, fromEmail, fromPassword);
            Log.i("GMail", "Connected to SMTP server.");

            Log.i("GMail", "Sending email to recipients: " + Arrays.toString(emailMessage.getAllRecipients()));
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();

            Log.i("GMail", "Email sent successfully.");
        } catch (Exception e) {
            Log.e("GMail", "Error while sending email: " + e.getMessage(), e);
        }
    }

}

