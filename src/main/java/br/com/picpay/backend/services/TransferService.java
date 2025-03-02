package br.com.picpay.backend.services;

import br.com.picpay.backend.data.dtos.TransferErrorResult;
import br.com.picpay.backend.data.dtos.TransferInformation;
import br.com.picpay.backend.data.dtos.TransferResult;
import br.com.picpay.backend.data.entities.User;
import br.com.picpay.backend.data.enums.KnownCurrencyOperations;
import br.com.picpay.backend.data.enums.TransferKnownStates;
import br.com.picpay.backend.exceptions.base.TransferException;
import br.com.picpay.backend.exceptions.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import net.xyzsd.dichotomy.Maybe;
import net.xyzsd.dichotomy.Result;
import net.xyzsd.dichotomy.trying.Try;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;


@Service
@RequiredArgsConstructor
public class TransferService {

    private final UserService userService;

    private final AuthorizationService authorizationService;

    private final NotificationService notificationService;

    private Result<User, Throwable> fetchUserTask(Long userId) {
        var task = Try.wrap(() -> Maybe.of(userService.findByUserId(userId)));

        if (task.isSuccess()) {
            if(task.expect().hasSome())
                return Result.ofOK(task.expect().expect());

            return Result.ofErr(new UserNotFoundException("User with specified ID not found on database"));
        }

        AtomicReference<Result<User, Throwable>> errResult = new AtomicReference<>();
        task.consumeErr(throwable -> {
            errResult.set(Result.ofErr(throwable));
        });
        return errResult.get();
    }

    public Result<TransferResult, TransferErrorResult> TransferCurrency(@NotNull TransferInformation transferInformation) {

        var maybeSourceUser = fetchUserTask(transferInformation.payer());
        var maybeDestinationUse  = fetchUserTask(transferInformation.payee());

        // Validate both users existence on database
        if(maybeSourceUser.isErr()) {

            AtomicReference<Throwable> cause = new AtomicReference<>();
            maybeSourceUser.fold(user -> user, throwable -> {
                cause.set(throwable);
                return throwable;
            });

            return Result.ofErr(new TransferErrorResult(transferInformation,
                    new TransferException(
                            "Invalid user information provided",
                            HttpStatus.FORBIDDEN,
                            TransferKnownStates.InvalidUserInformation,
                            transferInformation,
                            cause.get())));
        }

        else if(maybeDestinationUse.isErr()) {

            AtomicReference<Throwable> cause = new AtomicReference<>();
            maybeDestinationUse.fold(user -> user, throwable -> {
                cause.set(throwable);
                return throwable;
            });

            return Result.ofErr(new TransferErrorResult(
                    transferInformation,
                    new TransferException(
                            "Invalid user information provided",
                            HttpStatus.FORBIDDEN,
                            TransferKnownStates.InvalidUserInformation,
                            transferInformation,
                            cause.get())));
        }

        if(!authorizationService.isAuthorized(maybeSourceUser.expect()))
        {
            return Result.ofErr(new TransferErrorResult(
                    transferInformation,
                    new TransferException(
                            "Unauthorized user",
                            HttpStatus.UNAUTHORIZED,
                            TransferKnownStates.UnauthorizedUser,
                            transferInformation)));
        }

        // Validate if both user types can accept transfers between them
        if(userService.IsTransferEnabled(maybeSourceUser.expect().getUserType(), maybeDestinationUse.expect().getUserType(), KnownCurrencyOperations.Payment).isErr())
            return Result.ofErr(new TransferErrorResult(
                    transferInformation,
                    new TransferException(
                            "Transfer is not enabled for provided user types",
                            HttpStatus.FORBIDDEN,
                            TransferKnownStates.InvalidUserKnownTypes,
                            transferInformation)));

        // TODO: Validate transfer source user currency value
        if(!this.ValidateTransferCurrencyAmount(transferInformation))
            return Result.ofErr(new TransferErrorResult(
                    transferInformation,
                    new TransferException(
                            "Transfer source user do not have the required value on account to complete the transfer",
                            HttpStatus.FORBIDDEN,
                            TransferKnownStates.InsufficientFunds,
                            transferInformation)));

        // Update users account currency total amounts
        return this.MaterializeTransfer(transferInformation);
    }

    private boolean ValidateTransferCurrencyAmount(@NotNull TransferInformation transferInformation) {
        var sourceUser = userService.findByUserId(transferInformation.payer());
        Long resultAmount = Math.subtractExact(sourceUser.getCurrencyAmount().longValue(), transferInformation.value().longValue());
        return resultAmount > 0;
    }


    @Transactional(propagation = REQUIRES_NEW)
    protected Result<TransferResult, TransferErrorResult> MaterializeTransfer(TransferInformation transferInformation)
    {
        Result<TransferResult, TransferErrorResult> result = null;
        try{
            // No need to check for both users existence since this method is only intended to
            // be called if all previous checks on transfer currency method passes (including user checks)
            Result.from(Optional.ofNullable(userService.findByUserId(transferInformation.payer())))
                    .consume(user -> {
                        user.setCurrencyAmount(user.getCurrencyAmount() - transferInformation.value());
                        userService.saveUser(user);
                    });

            Result.from(Optional.ofNullable(userService.findByUserId(transferInformation.payee())))
                    .consume(user -> {
                        user.setCurrencyAmount(user.getCurrencyAmount() + transferInformation.value());
                        userService.saveUser(user);
                    });

            result =  Result.ofOK(new TransferResult(transferInformation, TransferKnownStates.Completed));
        }
        catch (Exception e){
            result = Result.ofErr(new TransferErrorResult(transferInformation,new TransferException(e.getMessage(), TransferKnownStates.Canceled, transferInformation)));
        }

        if(result.isOK())
            // Notify destination user about transfer conclusion
            this.notificationService.notifyTransfer(userService.findByUserId(transferInformation.payee()));

        return result;
    }
}
