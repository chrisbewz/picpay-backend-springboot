package br.com.picpay.backend.data.dtos;

import br.com.picpay.backend.data.enums.UserKnownTypes;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


public record CreateUserDto(@NotNull String name, @Email String email, UserKnownTypes userType) {
}
