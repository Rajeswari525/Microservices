package com.eazybytes.loans.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponseDto {
    private String apiPath;
    private HttpStatus errorStatus;
    private String errorMessage;
    private LocalDateTime errorTime;
}
