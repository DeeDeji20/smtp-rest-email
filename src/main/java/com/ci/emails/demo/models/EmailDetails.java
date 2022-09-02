package com.ci.emails.demo.models;

import lombok.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailDetails {
    @NonNull
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private File attachment;
    private Set<String> copy = new HashSet<>();
}
