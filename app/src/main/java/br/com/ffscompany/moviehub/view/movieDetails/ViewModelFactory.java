package br.com.ffscompany.moviehub.view.movieDetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final String title;

    private final String overview;

    private final String rating;

    private final String releaseDate;

    public ViewModelFactory(String title, String overview, String rating, String releaseDate) {
        this.title = title;
        this.overview = overview;
        this.rating = "Imdb " + rating;
        this.releaseDate = releaseDate;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel.class)) {
            return (T) new MovieDetailsViewModel(title, overview, rating, releaseDate);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
