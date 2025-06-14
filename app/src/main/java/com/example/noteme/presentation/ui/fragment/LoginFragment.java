package com.example.noteme.presentation.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.noteme.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoRegister;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnGoRegister = view.findViewById(R.id.btn_go_register);

        btnLogin.setOnClickListener(v -> loginUser());

        btnGoRegister.setOnClickListener(v -> {
            // pindah ke RegisterFragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String pwd = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email harus diisi");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            etPassword.setError("Password harus diisi");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Login berhasil", Toast.LENGTH_SHORT).show();

                        // Simpan status login di SharedPreferences
                        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("is_logged_in", true);
                        editor.apply();

                        // Pindah ke NoteFragment setelah login sukses
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new NoteFragment())
                                .commit();

                    } else {
                        Toast.makeText(getContext(), "Login gagal: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

}
