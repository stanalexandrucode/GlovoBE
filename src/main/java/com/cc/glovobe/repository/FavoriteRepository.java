package com.cc.glovobe.repository;

import com.cc.glovobe.embededId.FavoriteId;
import com.cc.glovobe.model.Favorite;
import com.cc.glovobe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Favorite findFavoriteById(Long id);

    void deleteFavoriteById(FavoriteId favoriteId);

    List<Favorite> findFavoritesByUser(User user);

}
