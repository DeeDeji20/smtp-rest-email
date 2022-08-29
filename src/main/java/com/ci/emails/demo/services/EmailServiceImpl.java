package com.ci.emails.demo.services;

import com.ci.emails.demo.models.EmailDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
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

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String send(EmailDetails emailDetails) {

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

}
