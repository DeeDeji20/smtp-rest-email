package com.ci.emails.demo.services;

import com.ci.emails.demo.models.EmailDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Objects;


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
            if(hasAttachment(emailDetails.getAttachment())) addAttachment(mimeMessageHelper, emailDetails);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            log.info(">>>>>Message sent successfully");
            return "Message Sent Successfully";
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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

    private boolean hasAttachment(MultipartFile multipartFile) {
        return multipartFile != null;
    }

    private boolean hasCC(EmailDetails emailDetails) {
        return !emailDetails.getCopy().isEmpty();
    }

    private void addAttachment(MimeMessageHelper mimeMessageHelper, EmailDetails emailDetails)  {
        MultipartFile multipartFile = emailDetails.getAttachment();
        try(FileOutputStream fos= new FileOutputStream(Objects.requireNonNull(multipartFile.getOriginalFilename()))) {
            byte[] fb = multipartFile.getBytes();
            fos.write(fb);
            mimeMessageHelper.addAttachment(Objects.requireNonNull(multipartFile.getOriginalFilename()), new File(multipartFile.getOriginalFilename()));
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
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

    //        switch (emailDetails.getSender().toLowerCase()){
//            case "aedc":
//                mailSender.setUsername(env.getProperty("aedc.mail.username"));
//                mailSender.setPassword(env.getProperty("aedc.mail.password"));
//                break;
//            case "bedc":
//                mailSender.setUsername(env.getProperty("bedc.mail.username"));
//                mailSender.setPassword(env.getProperty("bedc.mail.password"));
//                break;
//            case "ekedp":
//                mailSender.setUsername("noreply@ekedp.com");
//                mailSender.setPassword("password");
//                break;
//            default:
//                mailSender.setUsername("noreply@cicod.com");
//                mailSender.setPassword("default");
//        }

//    try (var inputStream = mpf.getInputStream()) {
//            InputStreamReader reader = new InputStreamReader(inputStream);
//            BufferedReader br = new BufferedReader(reader);
//            String line = br.readLine();
//            PrintWriter writer = new PrintWriter(new FileWriter(mpf.getOriginalFilename()));
//            writer.println(line);
//            writer.close();
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }

}
