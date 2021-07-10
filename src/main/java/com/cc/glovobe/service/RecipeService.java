package com.cc.glovobe.service;

import com.cc.glovobe.dto.RecipeDto;
import com.cc.glovobe.exception.domain.UserNotFoundException;
import com.cc.glovobe.model.Recipe;
import com.cc.glovobe.model.User;
import com.cc.glovobe.repository.RecipeRepository;
import com.cc.glovobe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.cc.glovobe.service.FavoriteService.PRINCIPAL_NOT_FOUND_WITH_EMAIL;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void saveRecipe(Recipe recipeItem, String principalEmail) throws UserNotFoundException {
        User user = getUserByEmail(principalEmail);

        Recipe recipe = new Recipe();
        recipe.setName(recipeItem.getName());
        recipe.setDescription(recipeItem.getDescription());
        recipe.setImage(recipeItem.getImage());
        recipe.setUser(user);

        recipeRepository.save(recipe);
    }


    @Transactional
    public User getUserByEmail(String principalEmail) throws UserNotFoundException {
        User user = userRepository.findUserByEmail(principalEmail);
        if (user == null) {
            throw new UserNotFoundException(PRINCIPAL_NOT_FOUND_WITH_EMAIL + principalEmail);
        }
        return user;
    }

    public List<RecipeDto> getAllRecipes() {
        List<RecipeDto> recipeDTOList = new ArrayList<>();
        List<Recipe> list = recipeRepository.findAll();

        for (Recipe recipe : list) {
            RecipeDto recipeDto = new RecipeDto();
            recipeDto.setId(recipe.getId());
            recipeDto.setDescription(recipe.getDescription());
            recipeDto.setName(recipe.getName());
            recipeDto.setUserFirstName(recipe.getUser().getFirstName());
            recipeDto.setUserId(recipe.getUser().getId());
            recipeDto.setImage(recipe.getImage());
            recipeDTOList.add(recipeDto);
        }
        return recipeDTOList;
    }

    public boolean deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
        return true;
    }
}
