package br.com.picpay.backend.exceptions.base;

import org.springframework.http.HttpStatus;

public class InternalServerException extends CustomException{
    public InternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
