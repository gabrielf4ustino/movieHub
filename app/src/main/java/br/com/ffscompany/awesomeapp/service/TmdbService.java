package br.com.ffscompany.awesomeapp.service;

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
    private static final String API_KEY = "c7dd226ddb8f77f0536becda18f3c4fb";

    private final Options option;

    public TmdbService(@NonNull Context context, Options option) {
        super(context);
        this.option = option;
    }

    @Nullable
    @Override
    public List<BaseMovie> loadInBackground() {
        Tmdb tmdb = new Tmdb(API_KEY);
        Response<MovieResultsPage> response = null;
        switch (this.option) {
            case nowPlaying:
                try {
                    response = tmdb.moviesService().nowPlaying(null, null, null).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case popular:
                try {
                    response = tmdb.moviesService().popular(null, null, null).execute();
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
