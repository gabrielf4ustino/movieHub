package br.com.ffscompany.moviehub.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import br.com.ffscompany.moviehub.entity.FavoriteMovie;
import br.com.ffscompany.moviehub.entity.User;
import br.com.ffscompany.moviehub.entity.UserWithFavoriteMovies;

@Dao
public interface FavoriteMovieDao {

    @Transaction
    @Query("SELECT * FROM FavoriteMovie WHERE idMovie = :id LIMIT 1")
    FavoriteMovie getFavoriteMovies(Long id);

    @Transaction
    @Query("DELETE FROM FavoriteMovie WHERE idMovie = :id")
    void deleteById(Long id);

    @Update
    void update(FavoriteMovie favoriteMovie);

    @Insert
    void insert(FavoriteMovie favoriteMovie);

    @Delete
    void delete(FavoriteMovie favoriteMovie);

    @Query("SELECT * FROM FavoriteMovie")
    List<FavoriteMovie> listAll();
}
