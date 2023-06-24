package br.com.ffscompany.moviehub.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteMovie {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String name;
}
