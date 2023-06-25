package br.com.ffscompany.moviehub.view.sign;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.security.Key;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.database.LocalDatabase;
import br.com.ffscompany.moviehub.databinding.FragmentLoginBinding;
import br.com.ffscompany.moviehub.databinding.FragmentSignBinding;
import br.com.ffscompany.moviehub.entity.User;
import br.com.ffscompany.moviehub.entity.UserWithFavoriteMovies;
import br.com.ffscompany.moviehub.service.AESEncryption;

public class SignFragment extends Fragment {

    private LocalDatabase db;

    private FragmentSignBinding fragmentSignBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = LocalDatabase.getDatabase(this.getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSignBinding = FragmentSignBinding.inflate(inflater, container, false);

        Button goBackButton = fragmentSignBinding.goback;

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            goBackButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.goback_icon, 0, 0, 0);
        } else {
            goBackButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.goback_icon_black, 0, 0, 0);
        }

        EditText name = fragmentSignBinding.signName;
        EditText email = fragmentSignBinding.signEmail;
        EditText password = fragmentSignBinding.signPassword;
        EditText confirmPassword = fragmentSignBinding.signConfirmPassword;

        fragmentSignBinding.signButtonEnter.setOnClickListener(view -> {
            if (name.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("")) {
                Toast.makeText(this.getContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            } else {
                if (db.user().getUserWithFavoriteMovies(email.getText().toString()) != null) {
                    Toast.makeText(this.getContext(), "Email já cadastrado.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                    Toast.makeText(this.getContext(), "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Key key = AESEncryption.generateKey();
                    String passwordHash = String.valueOf(AESEncryption.encrypt(password.getText().toString(), key));
                    db.user().insert(new User(name.getText().toString(), email.getText().toString(), passwordHash, AESEncryption.keyToString(key)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this.getContext(), "Usuário cadastrado com sucesso.", Toast.LENGTH_SHORT).show();

                setUserSession(email.getText().toString());

                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_navigation_sign_to_navigation_home);
            }
        });

        fragmentSignBinding.goback.setOnClickListener(view -> {
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_navigation_sign_to_navigation_login);
        });
        return fragmentSignBinding.getRoot();
    }

    void setUserSession(String email){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("SessionLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logged", email);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentSignBinding = null;
    }
}