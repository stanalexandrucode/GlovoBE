package com.cc.glovobe.service;

import com.cc.glovobe.dto.FavoriteDto;
import com.cc.glovobe.exception.domain.UserNotFoundException;
import com.cc.glovobe.model.User;
import com.cc.glovobe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    public static final String PRINCIPAL_NOT_FOUND_WITH_EMAIL = "Principal not found with email: ";
    private final UserRepository userRepository;

    @Autowired
    public FavoriteService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void addFavMeal(FavoriteDto favoriteDto, String principalEmail) {


    }

    private User getUserByEmail(String principalEmail) throws UserNotFoundException {
        User user = userRepository.findUserByEmail(principalEmail);
        if (user == null) {
            throw new UserNotFoundException(PRINCIPAL_NOT_FOUND_WITH_EMAIL + principalEmail);
        }
        return user;
    }
}
