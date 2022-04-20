package com.jakubartlomiej.workingtimeregistration2.repository;

import com.jakubartlomiej.workingtimeregistration2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    @Modifying
    @Transactional
    @Query(value = "update User u set " +
            "u.login = :login, " +
            "u.cardNumber = :cardNumber, " +
            "u.firstName = :firstName, " +
            "u.lastName = :lastName " +
            "where u.id = :id")
    void updateWithoutPasswordAndRoles(@Param("id") long id,
                                       @Param("login") String login,
                                       @Param("cardNumber") String cardNumber,
                                       @Param("lastName") String lastName,
                                       @Param("firstName") String firstName);

    @Transactional
    @Modifying
    @Query(value = "insert into User (login, password, user_id) values (:login, :password, :user_id)",
            nativeQuery = true)
    void addLogin(@Param("login") String login,
                  @Param("password") String password,
                  @Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query(value = "delete from User where user_id = :user_id",
            nativeQuery = true)
    void deleteOnlyUserById(@Param("user_id") Long user_id);
}