package br.com.picpay.backend.data.repositories;

import br.com.picpay.backend.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Exposes common database operations related to UserModel objects
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * @param email Email to find for user on database
     * @return A UserModel object containing basic information about requested user from database
     */
    public User findByUserEmail(String email);


    /**
     * @param userName Name of desired user to recover from database collection
     * @return A UserModel object containing basic information about requested user from database
     */
    public User findByUserName(String userName);

    public User findByUserId(Long userId);

}
