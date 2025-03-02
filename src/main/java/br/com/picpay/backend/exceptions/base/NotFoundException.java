package br.com.picpay.backend.exceptions.base;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException{

    public NotFoundException() {
        super("Resource not found", HttpStatus.NOT_FOUND);
    }
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
