package com.sprinklr.Cronjob.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService{

    @Override
    public void sendmail(String reportUrl) {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gurusaikumar2002@gmail.com", "ljpnxpnxpfiefymh");
            }
        });
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("gurusaikumar2002@gmail.com", false));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("epanagandla.kumar@sprinklr.com"));
            msg.setSubject("Some JStacks crossed threshold!!");
            msg.setContent("Please see full details at " + reportUrl, "text/html");
            msg.setSentDate(new Date());

            Transport.send(msg);
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

}
