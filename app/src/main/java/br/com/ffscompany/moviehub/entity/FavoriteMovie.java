package br.com.ffscompany.moviehub.entity;

import android.os.Build;
import android.os.Bundle;

import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.ZoneId;
import java.util.ArrayList;

@Entity
public class FavoriteMovie {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long idMovie;

    private String title;

    private String videoLink;

    private String posterLink;

    private String overview;

    private String rating;

    private String releaseDate;

    public FavoriteMovie() {
    }

    public FavoriteMovie(Long idMovie, String title, String videoLink, String posterLink, String overview, String rating, String releaseDate) {
        this.idMovie = idMovie;
        this.title = title;
        this.videoLink = videoLink;
        this.posterLink = posterLink;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getPosterLink() {
        return posterLink;
    }

    public void setPosterLink(String posterLink) {
        this.posterLink = posterLink;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
