package com.cc.glovobe.controller;

import com.cc.glovobe.configuration.userprincipalcontext.IAuthenticationFacade;
import com.cc.glovobe.dto.FavoriteDto;
import com.cc.glovobe.dto.MealDto;
import com.cc.glovobe.exception.ExceptionHandling;
import com.cc.glovobe.exception.domain.MealNotFoundException;
import com.cc.glovobe.exception.domain.UserNotFoundException;
import com.cc.glovobe.model.Favorite;
import com.cc.glovobe.model.HttpResponse;
import com.cc.glovobe.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/favorite")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController extends ExceptionHandling {

    public static final String FAVORITE_MEAL_DELETE_SUCCESSFULLY = "Favorite meal delete successfully";
    private final FavoriteService favoriteService;
    private final IAuthenticationFacade principal;

    @Autowired
    public FavoriteController(FavoriteService favoriteService, IAuthenticationFacade principal) {
        this.favoriteService = favoriteService;
        this.principal = principal;
    }


    @PostMapping("/addMeal")
    public ResponseEntity<Favorite> addFavMeal(@RequestBody FavoriteDto favoriteDto) throws UserNotFoundException, MealNotFoundException {
        String userPrincipal = principal.getAuthentication().getName();
        Favorite favorite = favoriteService.addFavMeal(favoriteDto, userPrincipal);
        return new ResponseEntity<>(favorite, OK);
    }

    @GetMapping()
    public ResponseEntity<List<MealDto>> getFavMeals() throws UserNotFoundException {
        String userPrincipal = principal.getAuthentication().getName();
        List<MealDto> allMeals = favoriteService.getAllFavoriteMeals(userPrincipal);
        return new ResponseEntity<>(allMeals, OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpResponse> deleteFavMeal(@PathVariable Long id) throws MealNotFoundException {
        String userPrincipal = principal.getAuthentication().getName();
        favoriteService.deleteFavMealById(id, userPrincipal);
        return response(OK, FAVORITE_MEAL_DELETE_SUCCESSFULLY);
    }


    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

}
