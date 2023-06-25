package br.com.ffscompany.moviehub.view.movieDetails;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.database.LocalDatabase;
import br.com.ffscompany.moviehub.databinding.FragmentMovieDetailsBinding;
import br.com.ffscompany.moviehub.entity.FavoriteMovie;
import br.com.ffscompany.moviehub.service.VideoTmdbService;
import br.com.ffscompany.moviehub.service.WatchProviderService;

public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private FragmentMovieDetailsBinding binding;

    private MovieDetailsViewModel movieDetailsViewModel;

    private Bundle bundle;

    private Integer movieId;
    private String title;

    private String overview;

    private String rating;

    private String releaseDate;

    private Boolean favorite = false;

    private LocalDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = getArguments();
        this.movieId = bundle.getInt("id");
        this.title = bundle.getString("title");
        this.overview = bundle.getString("overview");
        this.rating = bundle.getString("rating");
        this.releaseDate = bundle.getString("releaseDate");
        db = LocalDatabase.getDatabase(this.getContext());
        LoaderManager.getInstance(this).initLoader(movieId, null, this).forceLoad();
        LoaderManager.getInstance(this).initLoader(0, null, this).forceLoad();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false);
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        movieDetailsViewModel = new ViewModelProvider(this, new ViewModelFactory(bundle.getString("title"), bundle.getString("overview"), bundle.getString("rating"), bundle.getString("release_date"))).get(MovieDetailsViewModel.class);

        if (db.favoriteMovie().getFavoriteMovies(Integer.toUnsignedLong(movieId)) != null)
            this.favorite = true;

        Button goBackButton = binding.goback;
        Button favoriteButton = binding.favoriteButton;

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            goBackButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.goback_icon, 0, 0, 0);
            if (favorite)
                favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_white_24dp, 0, 0, 0);
            else
                favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_border_white_24dp, 0, 0, 0);
        } else {
            goBackButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.goback_icon_black, 0, 0, 0);
            if (favorite)
                favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_black_24dp, 0, 0, 0);
            else
                favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_border_black_24dp, 0, 0, 0);
        }

        favoriteButton.setOnClickListener(view -> {
            if (!favorite) {
                db.favoriteMovie().insert(new FavoriteMovie(Integer.toUnsignedLong(movieId), title, null, null, overview, rating, releaseDate));
            } else {
                db.favoriteMovie().deleteById(Integer.toUnsignedLong(movieId));
            }
            favorite = !favorite;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                if (favorite)
                    favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_white_24dp, 0, 0, 0);
                else
                    favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_border_white_24dp, 0, 0, 0);
            } else {
                if (favorite)
                    favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_black_24dp, 0, 0, 0);
                else
                    favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_border_black_24dp, 0, 0, 0);
            }
        });

        View root = binding.getRoot();

        final TextView movieTitle = binding.movieTitle;
        movieDetailsViewModel.getTitle().observe(getViewLifecycleOwner(), movieTitle::setText);

        final TextView movieSynopsis = binding.movieOverview;
        movieDetailsViewModel.getOverview().observe(getViewLifecycleOwner(), overview -> {
            if (!Objects.equals(overview, "")) {
                movieSynopsis.setText(overview);
            } else {
                movieSynopsis.setText("Sinopse não disponível");
            }
        });

        final TextView movieRating = binding.movieRating;
        movieDetailsViewModel.getRating().observe(getViewLifecycleOwner(), movieRating::setText);

        final TextView movieReleaseDate = binding.movieReleaseDate;
        movieDetailsViewModel.getReleaseDate().observe(getViewLifecycleOwner(), movieReleaseDate::setText);

        Button goback = binding.goback;
        goback.setOnClickListener(view -> {
            NavController navController = NavHostFragment.findNavController(getParentFragment());
            navController.popBackStack();
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        binding = null;
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case 0:
                return new WatchProviderService(requireContext(), movieId);
            default:
                return new VideoTmdbService(requireContext(), id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        switch (loader.getId()) {
            case 0:
                TextView watchProvider = binding.movieWatchProvider;
                if (!data.equals("")) {
                    watchProvider.setText("Disponível em: " + data);
                } else {
                    watchProvider.setVisibility(View.GONE);
                }
                break;
            default:
                WebView webView = binding.playerView;

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setMediaPlaybackRequiresUserGesture(false);
                webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

                String videoUrl = "https://www.youtube.com/embed/" + data;
                String html = "<html>\n" +
                        "  <body style=\"margin:0;padding:0;\">\n" +
                        "    <iframe width=\"100%\" height=\"100%\" src=\"" + videoUrl + "\" frameborder=\"0\"></iframe>\n" +
                        "  </body>\n" +
                        "</html>\n";

                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient());

                webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        loader.reset();
    }
}