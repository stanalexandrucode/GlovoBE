package com.cc.glovobe.repository;

import com.cc.glovobe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String Email);

    User findUserById(Long id);
}
