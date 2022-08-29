package com.ci.emails.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private String attachment;
    private String copy;
}
