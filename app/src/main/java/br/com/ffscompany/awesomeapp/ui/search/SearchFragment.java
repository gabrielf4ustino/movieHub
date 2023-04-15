package br.com.ffscompany.awesomeapp.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.ffscompany.awesomeapp.R;
import br.com.ffscompany.awesomeapp.databinding.FragmentHomeBinding;
import br.com.ffscompany.awesomeapp.databinding.FragmentSearchBinding;
import br.com.ffscompany.awesomeapp.ui.search.card.MovieCardAdapter;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private RecyclerView recyclerSearch;
    private List<Movie> movies;
    private MovieCardAdapter adapter;
    private SearchView searchView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        recyclerSearch = binding.recyclerView;
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        movies = new ArrayList<>();
        adapter = new MovieCardAdapter(getActivity(),movies);
        recyclerSearch.setAdapter(adapter);

        searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Realiza a busca
                searchMovies(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Atualiza a lista com a nova consulta
                searchMovies(newText);
                return true;
            }
        });

        return binding.getRoot();
    }
    private void searchMovies(String query) {
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}