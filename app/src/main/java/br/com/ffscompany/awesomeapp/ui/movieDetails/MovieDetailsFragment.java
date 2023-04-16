package br.com.ffscompany.awesomeapp.ui.movieDetails;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import br.com.ffscompany.awesomeapp.R;
import br.com.ffscompany.awesomeapp.databinding.FragmentMovieDetailsBinding;
import br.com.ffscompany.awesomeapp.service.VideoTmdbService;

public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private FragmentMovieDetailsBinding binding;

    private MovieDetailsViewModel movieDetailsViewModel;

    private PlayerView playerView;

    private SimpleExoPlayer player;

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
        playerView = binding.playerView;
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

        initPlayer(data);

//        videoView.getSettings().setJavaScriptEnabled(true);
//        videoView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        videoView.loadUrl(data);
//        videoView.setWebChromeClient(new WebChromeClient());
    }

    private void initPlayer(String uri) {
        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);
        playYoutubeVideo(uri);
    }

    private void playYoutubeVideo(String uri) {
        Log.d("LINK", uri);

        new YouTubeExtractor(getContext()) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    int itag = 22;
                    String downloadUrl = ytFiles.get(itag).getUrl();
                }
            }
        }.extract(uri, false, true);
        new YouTubeExtractor(getContext()) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles != null) {
                    int videoTag = 137;
                    int audioTag = 140;
                    MediaSource audio = new ProgressiveMediaSource.Factory(
                            new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "AwesomeApp"))
                    ).createMediaSource(MediaItem.fromUri(ytFiles.get(audioTag).getUrl()));
                    MediaSource video = new ProgressiveMediaSource.Factory(
                            new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "AwesomeApp"))
                    ).createMediaSource(MediaItem.fromUri(ytFiles.get(videoTag).getUrl()));
                    player.setMediaSource(new MergingMediaSource(true, video, audio), true);
                    player.prepare();
                }
            }
        }.extract(uri, false, false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        loader.reset();
    }
}
