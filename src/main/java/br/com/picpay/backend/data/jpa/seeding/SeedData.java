package br.com.picpay.backend.data.jpa.seeding;

import br.com.picpay.backend.data.enums.UserKnownTypes;
import br.com.picpay.backend.data.mongo.documents.UserDocument;

import java.util.ArrayList;
import java.util.List;

public class SeedData {
    public static final List<UserDocument> users;

    static {
        users = new ArrayList<UserDocument>();

        users.add(UserDocument.builder()
                        .userEmail("jane.doe@gmail.com")
                        .userName("Jane Doe")
                        .userType(UserKnownTypes.Common)
                .build());

        users.add(UserDocument.builder()
                .userEmail("john.doe@gmail.com")
                .userName("John Doe")
                .userType(UserKnownTypes.Common)
                .build());

        users.add(UserDocument.builder()
                .userEmail("bob.doe@gmail.com")
                .userName("Bob Doe")
                .userType(UserKnownTypes.StoreOwner)
                .build());
    }
}
