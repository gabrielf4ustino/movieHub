package br.com.ffscompany.moviehub.view.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;
import java.util.Objects;

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.database.LocalDatabase;
import br.com.ffscompany.moviehub.databinding.FragmentProfileEditBinding;
import br.com.ffscompany.moviehub.entity.User;
import br.com.ffscompany.moviehub.service.AESEncryption;
import br.com.ffscompany.moviehub.service.Utils;


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
        TextView oldPassword = fragmentSignBinding.oldPassword;
        TextView newPassword = fragmentSignBinding.newPassword;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SessionLogin", Context.MODE_PRIVATE);

        Log.d("logged", sharedPreferences.getString("logged", ""));
        User user = db.user().getUserByEmail(sharedPreferences.getString("logged", ""));
        username.setText(user.getName());
        email.setText(user.getEmail());
        localPassword = user.getPassword();
        trueKey = user.getKey();

        fragmentSignBinding.signButtonEnter.setOnClickListener(v -> {
            if (username.getText().toString().equals("") || email.getText().toString().equals("") || localPassword.equals("")) {
                Toast.makeText(this.getContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            } else {
                if (!newPassword.getText().toString().equals("") && oldPassword.getText().toString().equals("")) {
                    Toast.makeText(this.getContext(), "Preencha a senha atual", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!oldPassword.getText().toString().isEmpty()) {
                    try {
                        trueLocalPassword = AESEncryption.decrypt(localPassword, AESEncryption.stringToKey(trueKey));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (!oldPassword.getText().toString().equals(trueLocalPassword)) {
                        Toast.makeText(this.getContext(), "Senha Atual incorreta.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newPassword.getText().toString().equals("")) {
                        Toast.makeText(this.getContext(), "Senha Nova Invalida", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                try {
                    String passwordHash = String.valueOf(AESEncryption.encrypt(newPassword.getText().toString(), AESEncryption.stringToKey(user.getKey())));
                    user.setName(username.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(passwordHash);
                    db.user().update(user);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this.getContext(), "Usuário atualizado com sucesso.", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_navigation_edit_profile_to_navigation_profile);
            }
        });

        Button goBackButton = fragmentSignBinding.goback;
        goBackButton.setOnClickListener(view -> {
            NavController navController = NavHostFragment.findNavController(getParentFragment());
            navController.popBackStack();
        });

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            goBackButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.goback_icon, 0, 0, 0);
        } else {
            goBackButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.goback_icon_black, 0, 0, 0);
        }

        requireActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);

        return fragmentSignBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        Utils.hideKeyboard(Objects.requireNonNull(getActivity()));
        fragmentSignBinding = null;
    }
}