package br.com.ffscompany.moviehub.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteMovie {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long idMovie;

    public FavoriteMovie() {
    }

    public FavoriteMovie(Long id, Long idMovie) {
        this.id = id;
        this.idMovie = idMovie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(Long idMovie) {
        this.idMovie = idMovie;
    }
}
