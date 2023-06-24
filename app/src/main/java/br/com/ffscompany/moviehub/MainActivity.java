package br.com.ffscompany.moviehub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.ffscompany.moviehub.databinding.ActivityMainBinding;
import br.com.ffscompany.moviehub.databinding.FragmentHomeBinding;
import br.com.ffscompany.moviehub.databinding.FragmentLoginBinding;
import br.com.ffscompany.moviehub.databinding.FragmentSignBinding;
import br.com.ffscompany.moviehub.entity.UserWithFavoriteMovies;
import br.com.ffscompany.moviehub.service.AESEncryption;
import br.com.ffscompany.moviehub.view.login.LoginFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}