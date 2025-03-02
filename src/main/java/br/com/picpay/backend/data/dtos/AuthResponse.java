package br.com.picpay.backend.data.dtos;

import br.com.picpay.backend.data.enums.AuthStatus;

public record AuthResponse(AuthData data, AuthStatus status) {
}

