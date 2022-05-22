package com.repositories;

import com.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    User findUserById(Long id);
    @Transactional
    User findUserByLoginAndPassword(String Login, String Password);
    @Query("SELECT MAX(id) FROM User")
    Long getMaxId();
//
//    @Query("SELECT User FROM User WHERE login = ?1 AND password = ?2")
//    User findUserByLoginAndPassword(String login, String password);

//    @Query("SELECT User FROM User WHERE firstName = ?1")
//    List<User> findUserByFirstNameCUSTOM(String firstName);
}
