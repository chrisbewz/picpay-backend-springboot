package br.com.picpay.backend.services;

import br.com.picpay.backend.data.dtos.TransferInformation;
import br.com.picpay.backend.data.dtos.TransferResult;
import br.com.picpay.backend.data.enums.KnownCurrencyOperations;
import br.com.picpay.backend.data.enums.TransferKnownStates;
import br.com.picpay.backend.exceptions.base.TransferException;
import br.com.picpay.backend.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import net.xyzsd.dichotomy.Maybe;
import net.xyzsd.dichotomy.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TransferService {

    private final UserRepository userRepository;

    private final UserService userService;

    public Result<TransferResult, TransferException> TransferCurrency(@NotNull TransferInformation transferInformation) {

        var maybeSourceUser = Maybe.of(userRepository.findByUserId(transferInformation.payer()));
        var maybeDestinationUse = Maybe.of(userRepository.findByUserId(transferInformation.payee()));

        // Validate both users existence on database
        if(!maybeSourceUser.hasSome() || !maybeDestinationUse.hasSome())
            return Result.ofErr(new TransferException(
                    "Invalid user provided",
                    TransferKnownStates.NonExistentUserId,
                    transferInformation));

        // Validate if both user types can accept transfers between them
        if(userService.IsTransferEnabled(maybeSourceUser.expect().getUserType(), maybeDestinationUse.expect().getUserType(), KnownCurrencyOperations.Payment).isErr())
            return Result.ofErr(new TransferException(
                    "Transfer is not enabled for provided user types",
                    TransferKnownStates.InvalidUserKnownTypes,
                    transferInformation));

        // TODO: Validate transfer source user currency value
        if(!Result.from(Optional.of(this.ValidateTransferCurrencyAmount(transferInformation))).isOK())
            return Result.ofErr(new TransferException(
                    "Transfer source user do not have the required value on account to complete the transfer",
                    TransferKnownStates.InsufficientFunds,
                    transferInformation));

        // Update users account currency total amounts
        return this.UpdateCurrencyAmount(transferInformation);
    }

    private boolean ValidateTransferCurrencyAmount(@NotNull TransferInformation transferInformation) {
        var sourceUser = userRepository.findByUserId(transferInformation.payer());
        Long resultAmount = Math.subtractExact(transferInformation.value().longValue(), sourceUser.getCurrencyAmount().longValue());
        return resultAmount > 0;
    }

    private @NotNull Result<TransferResult, TransferException> UpdateCurrencyAmount(TransferInformation transferInformation)
    {
        Result<TransferResult, TransferException> result = null;
        try{
            // No need to check for both users existence since this method is only intended to
            // be called if all previous checks on transfer currency method passes (including user checks)
            Result.from(Optional.ofNullable(userRepository.findByUserId(transferInformation.payer())))
                    .consume(user -> {
                        user.setCurrencyAmount(user.getCurrencyAmount() - transferInformation.value());
                        userRepository.save(user);
                    });

            Result.from(Optional.ofNullable(userRepository.findByUserId(transferInformation.payee())))
                    .consume(user -> {
                        user.setCurrencyAmount(user.getCurrencyAmount() + transferInformation.value());
                        userRepository.save(user);
                    });

            result =  Result.ofOK(new TransferResult(transferInformation, TransferKnownStates.Completed));
        }
        catch (Exception e){
            result = Result.ofErr(new TransferException(e.getMessage(), TransferKnownStates.Faulted, transferInformation));
        }

        return result;
    }
}
