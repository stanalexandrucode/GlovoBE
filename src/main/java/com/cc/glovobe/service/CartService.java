package com.cc.glovobe.service;

import com.cc.glovobe.dto.CartDto;
import com.cc.glovobe.embededId.CartId;
import com.cc.glovobe.exception.domain.CartItemNotFoundException;
import com.cc.glovobe.model.Cart;
import com.cc.glovobe.model.Meal;
import com.cc.glovobe.model.User;
import com.cc.glovobe.repository.CartRepository;
import com.cc.glovobe.repository.MealRepository;
import com.cc.glovobe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public CartService(MealRepository mealRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }


    @Transactional
    public void addCartItem(CartDto cartDto, String principalEmail) throws CartItemNotFoundException {
        User user = userRepository.findUserByEmail(principalEmail);
        Meal meal = mealRepository.findById(cartDto.getMealId()).orElseThrow(() ->
                new CartItemNotFoundException(String.format("cart with id %s not found", cartDto.getMealId())));

        CartId cartId = new CartId();
        cartId.setMealId(meal.getId());
        cartId.setUserId(user.getId());

        Cart cart = new Cart(
                cartId,
                user,
                meal,
                cartDto.getQuantity(),
                cartDto.getPrice()
        );
        cartRepository.save(cart);
    }
    @Transactional
    public List<CartDto> getAllCartItems(String principalEmail) {
        User user = userRepository.findUserByEmail(principalEmail);
        return transformToDto(cartRepository.findAllByUser(user));
    }

    private List<CartDto> transformToDto(List<Cart> cartItems) {
        return cartItems.
                stream()
                .map(item -> new CartDto(item.getId().getMealId(), item.getQuantity(), item.getClientSeenPrice()))
                .collect(Collectors.toList());
    }
    @Transactional
    public void removeCartItem(Long id, String principalEmail) throws CartItemNotFoundException {
        User user = userRepository.findUserByEmail(principalEmail);
        Meal meal = mealRepository.findById(id).orElseThrow(() ->
                new CartItemNotFoundException(String.format("cart with id %s not found", id)));

        CartId cartId = new CartId(user.getId(), meal.getId());
        cartRepository.deleteCartItemById(cartId);
    }

    @Transactional
    public void updateCart(List<CartDto> cartItems, String principalEmail) {
        User user = userRepository.findUserByEmail(principalEmail);


        List<Cart> newCartItems = cartItems.stream().map(item -> {
            Meal meal = null;
            try {
                meal = mealRepository.findById(item.getMealId()).orElseThrow(() ->
                        new CartItemNotFoundException(String.format("cart with id %s not found", item.getMealId())));
            } catch (CartItemNotFoundException e) {
                e.printStackTrace();
            }

            CartId cartId = new CartId();
            cartId.setMealId(meal.getId());
            cartId.setUserId(user.getId());

            return new Cart(
                    cartId,/**/
                    user,
                    meal,
                    item.getQuantity(),
                    item.getPrice());
        }).collect(Collectors.toList());

        cartRepository.deleteCartItemsByUserId(user.getId());
        cartRepository.saveAll(newCartItems);
    }


}
