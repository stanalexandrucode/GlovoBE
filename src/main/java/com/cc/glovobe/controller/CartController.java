package com.cc.glovobe.controller;

import com.cc.glovobe.configuration.userprincipalcontext.IAuthenticationFacade;
import com.cc.glovobe.dto.CartDto;
import com.cc.glovobe.exception.domain.CartItemNotFoundException;
import com.cc.glovobe.service.CartService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    private final CartService cartService;
    private final IAuthenticationFacade principal;

    @Autowired
    public CartController(CartService cartService, IAuthenticationFacade principal) {
        this.cartService = cartService;
        this.principal = principal;
    }


    @GetMapping()
    public ResponseEntity<List<CartDto>> getAllCartItems() {
        String userPrincipal = principal.getAuthentication().getName();
        List<CartDto> allCarts = cartService.getAllCartItems(userPrincipal);
        return new ResponseEntity<>(allCarts, OK);
    }

    @PostMapping("/add-meal")
    public ResponseEntity<Void> addNewCartProduct(@RequestBody @NotNull CartDto cartDto) throws CartItemNotFoundException {
        String userPrincipal = principal.getAuthentication().getName();
        cartService.addCartItem(cartDto, userPrincipal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-meal/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("id") Long id) throws CartItemNotFoundException {
        String userPrincipal = principal.getAuthentication().getName();
        cartService.removeCartItem(id, userPrincipal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update-cart")
    public ResponseEntity<Void> updateCartBeforePayment(@RequestBody @NotNull List<CartDto> cartItemDto) {
        String userPrincipal = principal.getAuthentication().getName();
        cartService.updateCart(cartItemDto, userPrincipal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
