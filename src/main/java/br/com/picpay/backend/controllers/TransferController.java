package br.com.picpay.backend.controllers;

import br.com.picpay.backend.data.dtos.TransferErrorResult;
import br.com.picpay.backend.data.dtos.TransferInformation;
import br.com.picpay.backend.data.dtos.TransferResult;
import br.com.picpay.backend.exceptions.base.CustomException;
import br.com.picpay.backend.exceptions.base.TransferException;
import br.com.picpay.backend.services.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.xyzsd.dichotomy.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.testng.annotations.Test;

@RestController
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResult> transferCurrency(@RequestBody @Valid TransferInformation transferInfo)
    {
        Result<TransferResult, TransferErrorResult> result = transferService.TransferCurrency(transferInfo);
        return result.fold(
                res -> ResponseEntity.status(200).body(res),
                err -> ResponseEntity.status(err.transferException().getStatusCode()).body(new TransferResult(transferInfo,err.transferException().getTransferState())));
    }
}
