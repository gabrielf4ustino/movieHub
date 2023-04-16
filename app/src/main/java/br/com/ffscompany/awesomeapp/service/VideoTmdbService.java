package br.com.ffscompany.awesomeapp.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.Videos;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class VideoTmdbService extends AsyncTaskLoader<String> {
    private static final Tmdb tmdb = new Tmdb("c7dd226ddb8f77f0536becda18f3c4fb");

    private final Integer movieId;

    public VideoTmdbService(@NonNull Context context, Integer movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        try {
            Response<Videos> response = tmdb.moviesService().videos(movieId, "pt-BR").execute();
            if (response != null && response.isSuccessful()) {
                List<Videos.Video> videos = response.body().results;
                for (Videos.Video video : videos) {
                    assert video.type != null;
                    if (video.type.equals("Trailer") && video.site.equals("YouTube")) {
                        return video.key;
                    }
                }
                if (videos.get(0).key != null)
                    return videos.get(0).key;
                return "";
            } else {
                try {
                    Log.e("TMDbApiClient", response.errorBody().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
