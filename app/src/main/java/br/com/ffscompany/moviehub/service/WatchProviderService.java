package br.com.ffscompany.moviehub.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.WatchProviders;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Response;

public class WatchProviderService extends AsyncTaskLoader<String> {
    private static final Tmdb tmdb = new Tmdb("c32957f584b399b761627b576a78e213");

    private final Integer movieId;

    public WatchProviderService(@NonNull Context context, Integer movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        Response<WatchProviders> response;
        try {
            response = tmdb.moviesService().watchProviders(movieId).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (response.isSuccessful()) {
            if (response.body().results.containsKey("BR") && response.body().results.get("BR").flatrate.size() > 0) {
                return Objects.requireNonNull(response.body().results.get("BR")).flatrate.get(0).provider_name;
            }
            return "";
        } else {
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

