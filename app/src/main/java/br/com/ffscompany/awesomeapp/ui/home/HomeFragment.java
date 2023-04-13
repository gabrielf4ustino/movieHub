package br.com.ffscompany.awesomeapp.ui.home;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerViewAdapter recyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<BaseMovie> nowPlayingMovies = null;
        List<BaseMovie> popularMovies = null;
        try {
            nowPlayingMovies = new TmdbService("nowPlaying").execute().get();
            popularMovies = new TmdbService("popular").execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RecyclerView slider = binding.slider;
        slider.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        slider.setAdapter(new SliderViewAdapter(getContext(), popularMovies));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(slider);


        RecyclerView nowPlayingRecyclerView = binding.nowPlayingMovies;
        nowPlayingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        nowPlayingRecyclerView.setAdapter(new RecyclerViewAdapter(getContext(), nowPlayingMovies));

        RecyclerView popularMoviesRecyclerView = binding.popularMovies;
        popularMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        popularMoviesRecyclerView.setAdapter(new RecyclerViewAdapter(getContext(), popularMovies));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}