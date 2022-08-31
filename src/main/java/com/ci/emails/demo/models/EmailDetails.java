package com.ci.emails.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private String attachment;
    private Set<String> copy = new HashSet<>();
}
