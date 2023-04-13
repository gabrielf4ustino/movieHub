package br.com.ffscompany.awesomeapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.ffscompany.awesomeapp.databinding.FragmentHomeBinding;
import br.com.ffscompany.awesomeapp.databinding.FragmentNotificationsBinding;
import br.com.ffscompany.awesomeapp.service.TmdbService;
import br.com.ffscompany.awesomeapp.ui.home.recyclerView.RecyclerViewAdapter;
import br.com.ffscompany.awesomeapp.ui.notifications.NotificationsViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerViewAdapter recyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TmdbService tmdbDbService = new TmdbService();
        List<BaseMovie> nowPlayingMovies = null;
        List<BaseMovie> popularMovies = null;
        try {
            nowPlayingMovies = tmdbDbService.execute("nowPlaying").get();
//            popularMovies = tmdbDbService.execute("popular").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RecyclerView nowPlayingRecyclerView = binding.nowPlayingMovies;
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), nowPlayingMovies);
        nowPlayingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        nowPlayingRecyclerView.setAdapter(recyclerViewAdapter);

//        RecyclerView popularMoviesRecyclerView = binding.popularMovies;
//        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), popularMovies);
//        popularMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//        popularMoviesRecyclerView.setAdapter(recyclerViewAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}