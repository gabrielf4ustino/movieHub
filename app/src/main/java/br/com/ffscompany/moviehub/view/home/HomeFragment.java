package br.com.ffscompany.moviehub.view.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.databinding.FragmentHomeBinding;
import br.com.ffscompany.moviehub.service.Options;
import br.com.ffscompany.moviehub.service.TmdbService;
import br.com.ffscompany.moviehub.view.home.recyclerView.MovieViewAdapter;
import br.com.ffscompany.moviehub.view.home.slider.SliderViewAdapter;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<BaseMovie>> {

    private FragmentHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoaderManager.getInstance(this).initLoader(0, null, this).forceLoad();

        LoaderManager.getInstance(this).initLoader(1, null, this).forceLoad();

        LoaderManager.getInstance(this).initLoader(2, null, this).forceLoad();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    public Loader<List<BaseMovie>> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case 0:
                return new TmdbService(requireContext(), Options.NOW_PLAYING);
            case 1:
                return new TmdbService(requireContext(), Options.POPULAR);
            case 2:
                return new TmdbService(requireContext(), Options.UP_COMING);
            default:
                // Retorna null caso o ID seja inválido
                return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<BaseMovie>> loader, List<BaseMovie> movies) {
        switch (loader.getId()) {
            case 0:
                if (movies != null) {
                    RecyclerView slider = binding.slider;
                    slider.setOnFlingListener(null);
                    slider.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                    slider.setAdapter(new SliderViewAdapter(getContext(), movies, movie -> {
                        navigate(movie, R.id.action_navigation_home_to_navigation_movie_details);
                    }));
                    SnapHelper snapHelper = new LinearSnapHelper();
                    snapHelper.attachToRecyclerView(slider);

                    RecyclerView nowPlayingMovies = binding.nowPlayingMovies;
                    nowPlayingMovies.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                    nowPlayingMovies.setAdapter(new MovieViewAdapter(getContext(), movies, movie -> navigate(movie, R.id.action_navigation_home_to_navigation_movie_details)));
                }
                break;
            case 1:
                if (movies != null) {
                    RecyclerView gridMovies = binding.popularMovies;
                    gridMovies.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                    gridMovies.setAdapter(new MovieViewAdapter(getContext(), movies, movie -> navigate(movie, R.id.action_navigation_home_to_navigation_movie_details)));
                }
                break;
            case 2:
                RecyclerView upComingMovies = binding.upComingMovies;
                upComingMovies.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                upComingMovies.setAdapter(new MovieViewAdapter(getContext(), movies, movie -> navigate(movie, R.id.action_navigation_home_to_navigation_movie_details)));
                break;
        }
    }

    private void navigate(BaseMovie movie, int id) {
        Bundle args = new Bundle();
        args.putInt("id", movie.id);
        args.putString("title", movie.title);
        args.putString("overview", movie.overview);
        args.putIntegerArrayList("genres", (ArrayList<Integer>) movie.genre_ids);
        args.putString("rating", String.valueOf(movie.vote_average));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            args.putString("release_date", String.valueOf(movie.release_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear()));
        }
        assert getParentFragment() != null;
        NavHostFragment.findNavController(getParentFragment()).navigate(id, args);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<BaseMovie>> loader) {
        loader.reset();
    }
}