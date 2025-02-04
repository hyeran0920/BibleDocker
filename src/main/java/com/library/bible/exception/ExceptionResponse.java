package com.library.bible.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionResponse {
    private final int status;
    private final String error;    
    private final String message;
    private final String detailMessage;
    private final LocalDateTime timestamp;
    private final String path;
}
