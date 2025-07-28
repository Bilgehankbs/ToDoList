package com.ornek.todolist.repo;

import  com.ornek.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * Kullanıcı veritabanı işlemleri
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Email’e göre kullanıcı bulmak için:
     * spring-data JPA method name convention ile otomatik implement edilir.
     */
    Optional<User> findByEmail(String email);

}
