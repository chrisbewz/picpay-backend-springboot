package br.com.picpay.backend.exceptions.base;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    @Getter(AccessLevel.PUBLIC)
    private final HttpStatus statusCode;

    public CustomException(String message) {
        super(message);
        this.statusCode = HttpStatus.BAD_REQUEST;
    }

    public CustomException(Throwable cause) {
        super(cause);
        this.statusCode = HttpStatus.BAD_REQUEST;
    }

    public CustomException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public CustomException(String message, HttpStatus statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
}
