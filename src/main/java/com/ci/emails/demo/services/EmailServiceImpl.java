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
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService{
    @Autowired
    private final JavaMailSender javaMailSender;
    @Autowired
    private JavaMailSenderImpl mailSender ;


    @Autowired
    private Environment env;
    private final Multipart multipart = new MimeMultipart();

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public String send(EmailDetails emailDetails) {
        getSenderDetails(emailDetails);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try{
            mimeMessageHelper = setMessageDetails(emailDetails, mimeMessage);
            if(hasCC(emailDetails)){
                emailDetails.getCopy().forEach(cc->{
                    try {
                        mimeMessageHelper.addCc(cc);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            if(hasAttachment(emailDetails)) addAttachment(emailDetails, mimeMessage, multipart);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        log.info(">>>>>Message sent successfully");
        return "Message Sent Successfully";
    }

    private MimeMessageHelper setMessageDetails(EmailDetails emailDetails, MimeMessage mimeMessage) throws MessagingException {
        MimeMessageHelper mimeMessageHelper;
        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(emailDetails.getSender());
        mimeMessageHelper.setTo(emailDetails.getRecipient());
        mimeMessageHelper.setText(emailDetails.getBody());
        mimeMessageHelper.setSubject(emailDetails.getSubject());
        return mimeMessageHelper;
    }

    private boolean hasAttachment(EmailDetails emailDetails) {
        return emailDetails.getAttachment() != null;
    }

    private boolean hasCC(EmailDetails emailDetails) {
        return !emailDetails.getCopy().isEmpty();
    }

    private void addAttachment(EmailDetails emailDetails, MimeMessage mimeMessage, Multipart multipart) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(emailDetails.getAttachment());
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(new DataHandler(source).getName());
        multipart.addBodyPart(messageBodyPart);
        mimeMessage.setContent(multipart);
    }

    private void getSenderDetails(EmailDetails emailDetails) {
        String sender = emailDetails.getSender();
        String username;
        String password;
        if (sender.equals("aedc")||sender.equals("bedc")||sender.equals("ekedp")) {
             username = env.getProperty(sender+"."+"mail.username");
             password = env.getProperty(sender+"."+"mail.password");
        }else{
             username = env.getProperty("default.mail.username");
             password = env.getProperty("default.mail.password");
        }

        mailSender.setUsername(username);
        mailSender.setPassword(password);

    }

}
