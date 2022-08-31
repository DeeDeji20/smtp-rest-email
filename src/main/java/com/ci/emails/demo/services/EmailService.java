package com.ci.emails.demo.services;

import com.ci.emails.demo.models.EmailDetails;

public interface EmailService {
    String send(EmailDetails emailDetails);
}
