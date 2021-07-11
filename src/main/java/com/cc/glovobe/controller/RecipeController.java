package com.cc.glovobe.controller;


import com.cc.glovobe.configuration.userprincipalcontext.IAuthenticationFacade;
import com.cc.glovobe.dto.RecipeDto;
import com.cc.glovobe.exception.ExceptionHandling;
import com.cc.glovobe.exception.domain.UserNotFoundException;
import com.cc.glovobe.model.Recipe;
import com.cc.glovobe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "http://localhost:3000")

public class RecipeController extends ExceptionHandling {

    private final RecipeService recipeService;
    private final IAuthenticationFacade principal;

    //the folder where pic are saved
    String UPLOAD_FOLDER = "W:\\CODECOOL\\ADVANCE\\56_tt_glovo\\src\\main\\static\\uploadImage\\";

    @Autowired
    public RecipeController(RecipeService recipeService, IAuthenticationFacade principal) {
        this.recipeService = recipeService;
        this.principal = principal;
    }

    @PostMapping("/addRecipe")
    public ResponseEntity<?> addNewRecipeItem(@RequestBody Recipe recipe) throws IllegalStateException, UserNotFoundException {
        String userPrincipal = principal.getAuthentication().getName();
        recipeService.saveRecipe(recipe, userPrincipal);
        return ResponseEntity.accepted().body("recipe added");
    }

    @GetMapping("/all")
    public List<RecipeDto> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteRecipe(@PathVariable Long id) {
        var isRemoved = recipeService.deleteRecipeById(id);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
