package com.ci.emails.demo.controller;

import com.ci.emails.demo.models.EmailDetails;
import com.ci.emails.demo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody EmailDetails emailDetails){
        try{
            String message = emailService.send(emailDetails);
            ApiResponse apiResponse = ApiResponse.builder()
                    .isSuccessful(true)
                    .response(message)
                    .statusCode(201)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }catch(RuntimeException exception){
            ApiResponse apiResponse = ApiResponse.builder()
                    .isSuccessful(false)
                    .response(exception.getMessage())
                    .statusCode(404)
                    .build()
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
