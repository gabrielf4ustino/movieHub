package br.com.ffscompany.moviehub.view.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.database.LocalDatabase;
import br.com.ffscompany.moviehub.databinding.FragmentLoginBinding;
import br.com.ffscompany.moviehub.entity.UserWithFavoriteMovies;
import br.com.ffscompany.moviehub.service.AESEncryption;

public class LoginFragment extends Fragment {

    private LocalDatabase db;

    private FragmentLoginBinding fragmentLoginBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = LocalDatabase.getDatabase(this.getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false);

        if (isUserLoggedIn()) {
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_login_fragment_to_navigation_home);
            return new View(requireContext());
        }

        EditText email = fragmentLoginBinding.loginEmail;
        EditText password = fragmentLoginBinding.loginPassword;

        fragmentLoginBinding.loginButtonEnter.setOnClickListener(view -> {
            if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
                Toast.makeText(this.getContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            } else {
                UserWithFavoriteMovies user = db.user().getUserWithFavoriteMovies(email.getText().toString());
                if (user != null) {
                    try {
                        if (email.getText().toString().equals(user.user.getEmail()) && password.getText().toString().equals(AESEncryption.decrypt(user.user.getPassword(), AESEncryption.stringToKey(user.user.getKey())))) {
                            setUserSession(email.getText().toString());
                            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_login_fragment_to_navigation_home);
                        } else {
                            Toast.makeText(this.getContext(), "E-mail e/ou senha incorretos.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(this.getContext(), "Usuário não cadastrado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView sign = fragmentLoginBinding.loginButtonSign;
        sign.setOnClickListener(view -> NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_navigation_login_to_navigation_sign));
        return fragmentLoginBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideKeyboard(Objects.requireNonNull(getActivity()));
        fragmentLoginBinding = null;
    }

    private void setUserSession(String email){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("SessionLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logged", email);
        editor.apply();
    }

    private boolean isUserLoggedIn() {
        // Verifique se as informações da sessão (exemplo: nome de usuário) estão presentes nas SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SessionLogin", Context.MODE_PRIVATE);
        return sharedPreferences.contains("logged");
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}