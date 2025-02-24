package br.com.picpay.backend.controllers;

import br.com.picpay.backend.data.dtos.TransferInformation;
import br.com.picpay.backend.data.dtos.TransferResult;
import br.com.picpay.backend.exceptions.base.TransferException;
import br.com.picpay.backend.services.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.xyzsd.dichotomy.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transferCurrency(@RequestBody @Valid TransferInformation transferInfo)
    {
        Result<TransferResult, TransferException> result = transferService.TransferCurrency(transferInfo);
        return result.fold(
                res -> ResponseEntity.status(200).body(res),
                err -> ResponseEntity.status(err.getStatusCode()).body(err));
    }
}
