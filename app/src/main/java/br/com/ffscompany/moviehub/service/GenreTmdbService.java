package br.com.ffscompany.moviehub.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.enumerations.SortBy;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class GenreTmdbService extends AsyncTaskLoader<List<BaseMovie>> {

    private static final Tmdb tmdb = new Tmdb("c32957f584b399b761627b576a78e213");

    private final Integer genreId;

    public GenreTmdbService(@NonNull Context context, Integer genreId) {
        super(context);
        this.genreId = genreId;
    }

    @Nullable
    @Override
    public List<BaseMovie> loadInBackground() {
        Response<MovieResultsPage> response = null;
        try {
            response = tmdb.genreService().movies(genreId, "pt-BR", true, SortBy.POPULARITY_DESC).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null && response.isSuccessful()) {
            return response.body().results;
        } else if (response != null) {
            try {
                Log.e("TMDbApiClient", response.errorBody().string());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
