package br.com.ffscompany.moviehub.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class TmdbService extends AsyncTaskLoader<List<BaseMovie>> {
    private static final Tmdb tmdb = new Tmdb("c32957f584b399b761627b576a78e213");

    private final Options option;

    public TmdbService(@NonNull Context context, Options option) {
        super(context);
        this.option = option;
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
