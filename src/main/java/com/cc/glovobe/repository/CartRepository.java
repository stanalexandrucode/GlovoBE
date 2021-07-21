package com.cc.glovobe.repository;


import com.cc.glovobe.embededId.CartId;
import com.cc.glovobe.model.Cart;
import com.cc.glovobe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT * FROM cart_items WHERE user_id = ?1", nativeQuery = true)
    List<Cart> findAllCartItemsByUserId(Long userId);

    List<Cart> findAllByUser(User user);

    @Modifying
    @Transactional
    void deleteCartItemById(CartId cartId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM cart_items WHERE user_id = ?1", nativeQuery = true)
    int deleteCartItemsByUserId(Long userId);
}
