package br.com.picpay.backend.data.dtos;

import br.com.picpay.backend.exceptions.base.TransferException;

/**
 * Record providing detailed information about a given failed transfer attempt
 * @param transferInformation Request source transfer information
 * @param transferException Error data related to failed transfer attempt
 */
public record TransferErrorResult(TransferInformation transferInformation, TransferException transferException){
}
