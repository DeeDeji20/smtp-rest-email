package com.ci.emails.demo.models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile attachment;
    private Set<String> copy = new HashSet<>();
}
