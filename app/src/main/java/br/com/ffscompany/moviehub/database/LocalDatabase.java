package br.com.ffscompany.moviehub.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.ffscompany.moviehub.dao.UserDao;


@Database(entities = {UserDao.class}, version = 3)
public abstract class LocalDatabase extends RoomDatabase {
    private static LocalDatabase INSTANCE;

    public static LocalDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    LocalDatabase.class, "Users").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public abstract UserDao userModel();
}
