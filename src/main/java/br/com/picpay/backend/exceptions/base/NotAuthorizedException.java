package br.com.picpay.backend.exceptions.base;

import org.springframework.http.HttpStatus;

public class NotAuthorizedException extends CustomException {

    public NotAuthorizedException() {
        super("User is not authorized", HttpStatus.UNAUTHORIZED);
    }
    public NotAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
