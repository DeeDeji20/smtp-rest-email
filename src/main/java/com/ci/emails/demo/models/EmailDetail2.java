package com.ci.emails.demo.models;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class EmailDetail2 {
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private String attachment;
    private Set<String> ccs = new HashSet<>();
}
