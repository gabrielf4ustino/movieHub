package br.com.ffscompany.moviehub.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.ffscompany.moviehub.dao.FavoriteMovieDao;
import br.com.ffscompany.moviehub.dao.UserDao;
import br.com.ffscompany.moviehub.entity.FavoriteMovie;
import br.com.ffscompany.moviehub.entity.User;


@Database(entities = {User.class, FavoriteMovie.class}, version = 6)
public abstract class LocalDatabase extends RoomDatabase {
    private static LocalDatabase INSTANCE;

    public static LocalDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    LocalDatabase.class, "Users").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public abstract UserDao user();

    public abstract FavoriteMovieDao favoriteMovie();
}
