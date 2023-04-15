package br.com.ffscompany.awesomeapp.ui.movieDetails;

import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final String title;

    private final String overview;

    public ViewModelFactory(String title, String overview) {
        this.title = title;
        this.overview = overview;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel.class)) {
            return (T) new MovieDetailsViewModel(title, overview);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
