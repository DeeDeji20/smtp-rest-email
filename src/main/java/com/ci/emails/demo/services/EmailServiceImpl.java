package com.ci.emails.demo.services;

import com.ci.emails.demo.models.EmailDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService{
    @Autowired
    private final JavaMailSender javaMailSender;

    @Autowired
    private Environment environment;

    @Autowired
    private JavaMailSenderImpl mailSender ;


    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String send(EmailDetails emailDetails) {
        Properties props = configureMailingProperties();
        Session session = getSession(props);
        configureHost(props, session);

        getSenderDetails(emailDetails);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        Multipart multipart = new MimeMultipart();

        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(emailDetails.getSender());
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());
//            Add CC
            boolean hasCC = emailDetails.getCopy() == null;
            if(!hasCC){
                mimeMessageHelper.addCc(emailDetails.getCopy());
            }
//            Add attachment
            if(emailDetails.getAttachment() != null){
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(emailDetails.getAttachment());
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(new DataHandler(source).getName());
                multipart.addBodyPart(messageBodyPart);
                mimeMessage.setContent(multipart);
            }

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "Sent";
    }

    private void getSenderDetails(EmailDetails emailDetails) {
        switch (emailDetails.getSender()){
            case "aedc":
                mailSender.setUsername("deolaoladeji@gmail.com");
                mailSender.setPassword("cpdypwuuxhnopvmk");
                break;
            case "bedc":
                mailSender.setUsername("deedeji20@gmail.com");
                mailSender.setPassword("yqhxduxwchsstxcp");
                break;
            case "ekedp":
                mailSender.setUsername("admin@gmail.com");
                mailSender.setPassword("password");
                break;
            default:
                mailSender.setUsername("deolaoladeji@gmail.com");
                mailSender.setPassword("default");
        }
    }

    private void configureHost(Properties props, Session session) {
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setJavaMailProperties(props);
        mailSender.setSession(session);
    }

    private static Properties configureMailingProperties() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return props;
    }

    private Session getSession(Properties props) {
        return Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                mailSender.getUsername(), mailSender.getPassword());
                    }
                });
    }

}
