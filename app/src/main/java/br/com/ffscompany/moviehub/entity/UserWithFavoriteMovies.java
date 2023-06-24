package br.com.ffscompany.moviehub.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithFavoriteMovies {

    @Embedded
    public User user;

    @Relation(
            parentColumn = "id",
            entityColumn = "id"
    )
    public List<FavoriteMovie> favoriteMovieList;

    public UserWithFavoriteMovies() {
    }

    public UserWithFavoriteMovies(User user, List<FavoriteMovie> favoriteMovieList) {
        this.user = user;
        this.favoriteMovieList = favoriteMovieList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<FavoriteMovie> getFavoriteMovieList() {
        return favoriteMovieList;
    }

    public void setFavoriteMovieList(List<FavoriteMovie> favoriteMovieList) {
        this.favoriteMovieList = favoriteMovieList;
    }
}
