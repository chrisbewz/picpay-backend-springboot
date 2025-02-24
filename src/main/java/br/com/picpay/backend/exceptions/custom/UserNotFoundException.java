package br.com.picpay.backend.exceptions.custom;

import br.com.picpay.backend.exceptions.base.CustomException;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
