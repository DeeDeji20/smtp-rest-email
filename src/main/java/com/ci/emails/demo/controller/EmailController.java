package com.ci.emails.demo.controller;

import com.ci.emails.demo.models.EmailDetails;
import com.ci.emails.demo.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/send", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> sendMessage(@ModelAttribute EmailDetails emailDetails){
        try{
            String message = emailService.send(emailDetails);
            log.info(message);
            ApiResponse apiResponse = ApiResponse.builder()
                    .isSuccessful(true)
                    .response(message)
                    .statusCode(201)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }catch(RuntimeException exception){
            log.info(exception.getMessage());
            exception.printStackTrace();
            ApiResponse apiResponse = ApiResponse.builder()
                    .isSuccessful(false)
                    .response(exception.getMessage())
                    .statusCode(404)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
