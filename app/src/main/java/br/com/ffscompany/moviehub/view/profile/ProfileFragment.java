package br.com.ffscompany.moviehub.view.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;
import java.util.Objects;

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.database.LocalDatabase;
import br.com.ffscompany.moviehub.databinding.FragmentProfileBinding;
import br.com.ffscompany.moviehub.databinding.FragmentSignBinding;
import br.com.ffscompany.moviehub.entity.User;
import br.com.ffscompany.moviehub.service.AESEncryption;


public class ProfileFragment extends Fragment {

    private LocalDatabase db;

    private FragmentProfileBinding fragmentSignBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = LocalDatabase.getDatabase(this.getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSignBinding = FragmentProfileBinding.inflate(inflater, container, false);
        TextView username = fragmentSignBinding.userName;
        TextView email = fragmentSignBinding.userEmail;

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SessionLogin", Context.MODE_PRIVATE);

        Log.d("logged", sharedPreferences.getString("logged", "aaa"));
        User user = db.user().getUserWithFavoriteMovies(sharedPreferences.getString("logged", "aaa")).user;
        username.setText(user.getName());
        email.setText(user.getEmail());
        Button editButton = fragmentSignBinding.configButton;
        Button logoutButton = fragmentSignBinding.logoutButton;

        editButton.setOnClickListener(v -> {

        });

        logoutButton.setOnClickListener(v -> {
            removeUserSession();
            requireActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_profile_to_navigation_login);
        });
        return fragmentSignBinding.getRoot();
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