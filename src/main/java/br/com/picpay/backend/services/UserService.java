package br.com.picpay.backend.services;

import br.com.picpay.backend.data.dtos.CreateUserDto;
import br.com.picpay.backend.data.enums.KnownCurrencyOperations;
import br.com.picpay.backend.data.enums.UserKnownTypes;
import br.com.picpay.backend.data.mongo.documents.UserDocument;
import br.com.picpay.backend.data.mongo.repositories.UserRepository;
import br.com.picpay.backend.exceptions.base.CustomException;
import br.com.picpay.backend.exceptions.base.TransferException;
import br.com.picpay.backend.exceptions.custom.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import net.xyzsd.dichotomy.Result;
import net.xyzsd.dichotomy.trying.Try;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final EnumMap<UserKnownTypes, EnumMap<UserKnownTypes, EnumSet<KnownCurrencyOperations>>> userOperationMap = new EnumMap<>(UserKnownTypes.class);

    private final UserRepository userRepository;

    static {
        for (UserKnownTypes UserKnownTypes : UserKnownTypes.values()) {
            userOperationMap.put(UserKnownTypes, new EnumMap<>(UserKnownTypes.class));
        }

        // Common user operations
        userOperationMap.get(UserKnownTypes.Common).put(UserKnownTypes.Common, EnumSet.of(KnownCurrencyOperations.Payment, KnownCurrencyOperations.Receipt));
        userOperationMap.get(UserKnownTypes.Common).put(UserKnownTypes.StoreOwner, EnumSet.of(KnownCurrencyOperations.Payment));

        // Store owner operations
        userOperationMap.get(UserKnownTypes.StoreOwner).put(UserKnownTypes.Common, EnumSet.of(KnownCurrencyOperations.Refund));
        userOperationMap.get(UserKnownTypes.StoreOwner).put(UserKnownTypes.StoreOwner, EnumSet.noneOf(KnownCurrencyOperations.class));
    }

    /**
     * @param sender Transfer source user type
     * @param receiver Transfer destination user type
     * @param operation Currency operation to valid against users
     * @return True if specified operation is supported by both users, otherwise a custom exception object with detailed information
     */
    public Result<Boolean,CustomException> IsTransferEnabled(UserKnownTypes sender, UserKnownTypes receiver, KnownCurrencyOperations operation) {
        EnumSet<KnownCurrencyOperations> allowedOperations = userOperationMap.get(sender).get(receiver);

        if(!(allowedOperations != null && allowedOperations.contains(operation)))
            return Result.ofErr(new TransferException("User types provided  cannot perform specified currency operation between them."));

        return Result.ofOK(true);
    }


}
