package com.cc.glovobe.repository;

import com.cc.glovobe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String Email);

    User findUserById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE app_user a " +
            "SET a.isEnabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);
}
