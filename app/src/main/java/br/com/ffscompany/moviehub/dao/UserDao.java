package br.com.ffscompany.moviehub.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.ffscompany.moviehub.entity.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    User getUserById(int id);

    @Query("SELECT * FROM User")
    List<User> getAll();

    @Update
    void update(User user);

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

}
