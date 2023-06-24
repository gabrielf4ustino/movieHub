package br.com.ffscompany.moviehub.view.movieDetails;

import android.os.Bundle;
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

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.databinding.FragmentMovieDetailsBinding;
import br.com.ffscompany.moviehub.service.VideoTmdbService;

public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private FragmentMovieDetailsBinding binding;

    private MovieDetailsViewModel movieDetailsViewModel;

    private View navHost;

    private Bundle bundle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.navHost = requireActivity().findViewById(R.id.nav_view);
        this.bundle = getArguments();

        LoaderManager.getInstance(this).initLoader(bundle.getInt("id"), null, this).forceLoad();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        navHost.setVisibility(View.GONE);

        movieDetailsViewModel = new ViewModelProvider(this, new ViewModelFactory(bundle.getString("title"), bundle.getString("overview"), bundle.getString("rating"), bundle.getString("release_date"))).get(MovieDetailsViewModel.class);

        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        final TextView movieTitle = binding.movieTitle;
        movieDetailsViewModel.getTitle().observe(getViewLifecycleOwner(), movieTitle::setText);

        final TextView movieSynopsis = binding.movieOverview;
        movieDetailsViewModel.getOverview().observe(getViewLifecycleOwner(), movieSynopsis::setText);

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
        binding = null;
        navHost.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new VideoTmdbService(requireContext(), id);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        WebView webView = binding.playerView;
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + data + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; fullscreen;\"></iframe>\n", "text/html", "utf-8");
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webView.setWebChromeClient(new WebChromeClient());

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

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        loader.reset();
    }
}