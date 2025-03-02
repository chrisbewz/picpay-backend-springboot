package br.com.picpay.backend.data.enums;

import lombok.Getter;

@Getter
public enum TransferKnownStates {

    // Success States
    Completed(true),
    Pending(true),

    // Error states
    Canceled(false),
    Faulted(false),
    InsufficientFunds(false),
    InvalidUserKnownTypes(false),
    InvalidUserInformation(false);

    private final boolean isSuccess;

    // Constructor to assign success/failure to each state
    private TransferKnownStates(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
