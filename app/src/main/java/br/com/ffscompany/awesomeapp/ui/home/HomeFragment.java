package br.com.ffscompany.awesomeapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.ffscompany.awesomeapp.databinding.FragmentHomeBinding;
import br.com.ffscompany.awesomeapp.service.TmdbService;
import br.com.ffscompany.awesomeapp.ui.home.recyclerView.RecyclerViewAdapter;
import br.com.ffscompany.awesomeapp.ui.home.slider.SliderViewAdapter;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<BaseMovie>> {

    private FragmentHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoaderManager.getInstance(this).initLoader(0, null, this).forceLoad();

        LoaderManager.getInstance(this).initLoader(1, null, this).forceLoad();
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
                // Retorna um novo loader para carregar dados do tipo 1
                return new TmdbService(requireContext(), "nowPlaying");
            case 1:
                // Retorna um novo loader para carregar dados do tipo 2
                return new TmdbService(requireContext(), "popular");
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
                    slider.setAdapter(new SliderViewAdapter(getContext(), movies));
                    SnapHelper snapHelper = new LinearSnapHelper();
                    snapHelper.attachToRecyclerView(slider);

                    RecyclerView nowPlayingRecyclerView = binding.nowPlayingMovies;
                    nowPlayingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                    nowPlayingRecyclerView.setAdapter(new RecyclerViewAdapter(getContext(), movies));
                }
                break;
            case 1:
                if (movies != null) {
                    RecyclerView popularMoviesRecyclerView = binding.popularMovies;
                    popularMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                    popularMoviesRecyclerView.setAdapter(new RecyclerViewAdapter(getContext(), movies));
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<BaseMovie>> loader) {
//        loader.reset();
    }

}