package com.ci.emails.demo.controller;

import com.ci.emails.demo.models.EmailDetails;
import com.ci.emails.demo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendMessage(@RequestBody EmailDetails emailDetails){
        return emailService.send(emailDetails);
    }
}
