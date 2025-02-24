package br.com.picpay.backend.exceptions.custom;

import br.com.picpay.backend.exceptions.base.ConflictException;

public class UserAlreadyExistsException extends ConflictException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
