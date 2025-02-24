package br.com.picpay.backend.data.dtos;

import br.com.picpay.backend.data.enums.TransferKnownStates;

/**
 * Record storing successful transfer information and state
 * @param transferInformation Transfer input information
 * @param transferState Actual transfer state after processing
 */
public record TransferResult(TransferInformation transferInformation, TransferKnownStates transferState) {
}

