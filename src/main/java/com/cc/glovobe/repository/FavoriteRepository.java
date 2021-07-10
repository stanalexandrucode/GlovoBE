package com.cc.glovobe.repository;

import com.cc.glovobe.embededId.FavoriteId;
import com.cc.glovobe.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Favorite findFavoriteById(Long id);

    void deleteFavoriteById(FavoriteId favoriteId);

}
