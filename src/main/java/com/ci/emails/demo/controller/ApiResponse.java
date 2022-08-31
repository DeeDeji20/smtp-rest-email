package com.ci.emails.demo.controller;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private String response;
    private boolean isSuccessful;
    private int statusCode;
}
