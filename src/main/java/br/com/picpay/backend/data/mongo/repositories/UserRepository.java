package br.com.picpay.backend.data.mongo.repositories;

import br.com.picpay.backend.data.mongo.documents.UserDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Exposes common database operations related to UserModel objects
 */
public interface UserRepository extends MongoRepository<UserDocument, Integer> {

    /**
     * @param email Email to find for user on database
     * @return A UserModel object containing basic information about requested user from database
     */
    @Query(value = "{userEmail: '?0'}")
    public UserDocument findByEmail(String email);


    /**
     * @param userName Name of desired user to recover from database collection
     * @return A UserModel object containing basic information about requested user from database
     */
    @Query(value = "{userName: '?0'}")
    public UserDocument findByUserName(String userName);

    @Query(value = "{userId: '?0'}")
    public UserDocument findByUserId(ObjectId userId);

}
