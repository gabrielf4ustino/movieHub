package br.com.ffscompany.awesomeapp.service;

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

public class TmdbService extends AsyncTaskLoader<List<BaseMovie>> {
    private static final Tmdb tmdb = new Tmdb("c7dd226ddb8f77f0536becda18f3c4fb");

    private final Options option;

    private final Integer genreId;

    public TmdbService(@NonNull Context context, Options option, int genreId) {
        super(context);
        this.option = option;
        this.genreId = genreId;
    }

    @Nullable
    @Override
    public List<BaseMovie> loadInBackground() {
        Response<MovieResultsPage> response = null;
        switch (this.option) {
            case NOW_PLAYING:
                try {
                    response = tmdb.moviesService().nowPlaying(null, "pt-BR", "BR").execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case POPULAR:
                try {
                    response = tmdb.moviesService().popular(null, "pt-BR", "BR").execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case UP_COMING:
                try {
                    response = tmdb.moviesService().upcoming(null, "pt-BR", "BR").execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case GENRE:
                try {
                    assert genreId != null;
                    response = tmdb.genreService().movies(genreId, "pt-BR", true, SortBy.POPULARITY_DESC).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
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

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
