package com.cc.glovobe.controller;

import com.cc.glovobe.configuration.userprincipalcontext.IAuthenticationFacade;
import com.cc.glovobe.dto.FavoriteDto;
import com.cc.glovobe.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final IAuthenticationFacade principal;

    @Autowired
    public FavoriteController(FavoriteService favoriteService, IAuthenticationFacade principal) {
        this.favoriteService = favoriteService;
        this.principal = principal;
    }


    @PostMapping("/addMeal")
    public ResponseEntity<Void> addFavMeal(@RequestBody FavoriteDto favoriteDto) {
        String userPrincipal = principal.getAuthentication().getName();
        favoriteService.addFavMeal(favoriteDto, userPrincipal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
