package br.com.picpay.backend.data.mongo.documents;

import br.com.picpay.backend.data.enums.UserKnownTypes;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User entity definition to store/retrieve from mongo database collection
 */
@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDocument {

    @Id
    private ObjectId userId;

    private String userName;

    private String userEmail;

    private UserKnownTypes userType;

    private Double currencyAmount;
}
