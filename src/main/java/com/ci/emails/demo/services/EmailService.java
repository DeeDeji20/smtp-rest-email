package com.ci.emails.demo.services;

import com.ci.emails.demo.models.EmailDetail2;
import com.ci.emails.demo.models.EmailDetails;

public interface EmailService {
    String send(EmailDetails emailDetails);
    String sendEmail(EmailDetail2 emailDetails);
}
