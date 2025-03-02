package br.com.picpay.backend.exceptions.base;

import br.com.picpay.backend.data.dtos.TransferInformation;
import br.com.picpay.backend.data.enums.TransferKnownStates;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class TransferException extends CustomException{

    private final Optional<TransferInformation> transferInformation;

    @Getter
    private final TransferKnownStates transferState;

    public TransferException(String message) {
        super(message);
        this.transferInformation = Optional.empty();
        this.transferState = TransferKnownStates.Faulted;
    }

    public TransferException(Throwable cause) {
        super(cause);
        this.transferInformation = Optional.empty();
        this.transferState = TransferKnownStates.Faulted;
    }

    public TransferException(String message, TransferInformation transferInformation) {
        super(message);
        this.transferInformation = Optional.of(transferInformation);
        this.transferState = TransferKnownStates.Faulted;
    }

    public TransferException(String message, HttpStatus status, TransferInformation transferInformation) {
        super(message, status);
        this.transferInformation = Optional.of(transferInformation);
        this.transferState = TransferKnownStates.Faulted;
    }

    public TransferException(String message, TransferKnownStates transferState, TransferInformation transferInformation) {
        super(message);
        this.transferInformation = Optional.of(transferInformation);
        this.transferState = transferState;
    }

    public TransferException(String message, HttpStatus status, TransferKnownStates transferState, TransferInformation transferInformation) {
        super(message, status);
        this.transferInformation = Optional.of(transferInformation);
        this.transferState = transferState;
    }

    public TransferException(String message, HttpStatus status, TransferKnownStates transferState, TransferInformation transferInformation, Throwable cause) {
        super(message, status, cause);
        this.transferInformation = Optional.of(transferInformation);
        this.transferState = transferState;
    }


}
