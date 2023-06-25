package br.com.ffscompany.moviehub.view.profile;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.database.LocalDatabase;
import br.com.ffscompany.moviehub.databinding.FragmentProfileBinding;
import br.com.ffscompany.moviehub.entity.FavoriteMovie;
import br.com.ffscompany.moviehub.entity.User;
import br.com.ffscompany.moviehub.view.home.recyclerView.FavoriteMovieViewAdapter;


public class ProfileFragment extends Fragment {

    private LocalDatabase db;
    private ImageView image;
    private FragmentProfileBinding fragmentSignBinding;
    private String imagePath;
    private User user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = LocalDatabase.getDatabase(this.getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSignBinding = FragmentProfileBinding.inflate(inflater, container, false);
        TextView username = fragmentSignBinding.userName;
        TextView email = fragmentSignBinding.userEmail;
        image = fragmentSignBinding.imageProfile;

        Button configButton = fragmentSignBinding.configButton;
        Button logoutButton = fragmentSignBinding.logoutButton;
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            configButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_white_24dp, 0, 0, 0);
            logoutButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.logout_white_24dp, 0, 0, 0);
        } else {
            configButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_black_24dp, 0, 0, 0);
            logoutButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.logout_black_24dp, 0, 0, 0);
        }

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SessionLogin", Context.MODE_PRIVATE);

        String userEmail = sharedPreferences.getString("logged", "");
        user = db.user().getUserByEmail(userEmail);
        if (user == null) {
            removeUserSession();
            requireActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_profile_to_navigation_login);
        } else {
            username.setText(user.getName());
            email.setText(user.getEmail());
            Button editButton = fragmentSignBinding.configButton;
            imagePath = user.getImagePath();
            if (imagePath != null) {
                image.setImageURI(Uri.parse(imagePath));
            }

            editButton.setOnClickListener(v -> {
                NavHostFragment.findNavController(this).navigate(R.id.action_navigation_profile_to_navigation_edit_profile);
            });

            logoutButton.setOnClickListener(v -> {
                removeUserSession();
                requireActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
                NavHostFragment.findNavController(this).navigate(R.id.action_navigation_profile_to_navigation_login);
            });

            List<FavoriteMovie> favoriteMovies = db.favoriteMovie().getFavoriteMoviesByUserEmail(userEmail);
            Log.d("FavoriteMovies", favoriteMovies.toString());
            RecyclerView movies = fragmentSignBinding.profileFavoriteMovies;
            movies.setLayoutManager(new GridLayoutManager(getContext(), 4));
            movies.setAdapter(new FavoriteMovieViewAdapter(getContext(), favoriteMovies, movie -> navigate(movie, R.id.action_navigation_profile_to_navigation_movie_details)));
        }
        image.setOnClickListener(v -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setType("image/*");
            startActivityForResult(iGallery, 1000);
        });

        return fragmentSignBinding.getRoot();
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1000 && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                image.setImageURI(selectedImage);
                imagePath = selectedImage.toString();
                user.setImagePath(imagePath);
                db.user().update(user);
            }
        }
    }
    private void navigate(FavoriteMovie movie, int id) {
        Bundle args = new Bundle();
        args.putInt("id", Math.toIntExact(movie.getIdMovie()));
        args.putString("title", movie.getTitle());
        args.putString("overview", movie.getOverview());
        args.putString("rating", movie.getRating());
        args.putString("release_date", movie.getReleaseDate());
        args.putString("poster_path", movie.getPosterLink());
        assert getParentFragment() != null;
        NavHostFragment.findNavController(getParentFragment()).navigate(id, args);
    }

    void removeUserSession() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SessionLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("logged");
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentSignBinding = null;
    }
}