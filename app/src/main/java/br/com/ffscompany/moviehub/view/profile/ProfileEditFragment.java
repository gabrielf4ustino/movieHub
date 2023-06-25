package br.com.ffscompany.moviehub.view.profile;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.database.LocalDatabase;
import br.com.ffscompany.moviehub.databinding.FragmentProfileEditBinding;
import br.com.ffscompany.moviehub.entity.User;
import br.com.ffscompany.moviehub.service.AESEncryption;


public class ProfileEditFragment extends Fragment {


    private LocalDatabase db;
    private String localPassword;
    private String trueLocalPassword;
    private String trueKey;
    private FragmentProfileEditBinding fragmentSignBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = LocalDatabase.getDatabase(this.getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSignBinding = FragmentProfileEditBinding.inflate(inflater, container, false);
        TextView username = fragmentSignBinding.signName;
        TextView email = fragmentSignBinding.signEmail;
        TextView oldpassword = fragmentSignBinding.oldpassword;
        TextView newpassword = fragmentSignBinding.newpassword;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SessionLogin", Context.MODE_PRIVATE);

        Log.d("logged", sharedPreferences.getString("logged", "aaa"));
        User user = db.userModel().getUserWithFavoriteMovies(sharedPreferences.getString("logged", "aaa")).user;
        username.setText(user.getName());
        email.setText(user.getEmail());
        //armazenar a senha antiga
        localPassword = user.getPassword();
        //armazena chave antiga
        trueKey = user.getKey();

        fragmentSignBinding.signButtonEnter.setOnClickListener(v -> {
            if (username.getText().toString().equals("") || email.getText().toString().equals("") || localPassword.equals("")) {
                Toast.makeText(this.getContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            } else {
                if(!oldpassword.getText().toString().isEmpty()){
                    try {
                        trueLocalPassword = String.valueOf(AESEncryption.decrypt(localPassword, AESEncryption.stringToKey(trueKey)));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if(!oldpassword.getText().toString().equals(trueLocalPassword)){
                        Toast.makeText(this.getContext(), "Senha Atual incorreta.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newpassword.getText().toString().equals("")) {
                        Toast.makeText(this.getContext(), "Senha Nova Invalida", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                try {
                    Key key = AESEncryption.generateKey();
                    String passwordHash = String.valueOf(AESEncryption.encrypt(newpassword.getText().toString(), key));
                    user.setName(username.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(passwordHash);
                    user.setKey(AESEncryption.keyToString(key));
                    db.userModel().update(user);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this.getContext(), "Usuário atualizado com sucesso.", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_navigation_edit_profile_to_navigation_profile);
            }
        });

        
        return fragmentSignBinding.getRoot();
    }
}