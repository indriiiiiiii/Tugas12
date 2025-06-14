package com.example.noteme.presentation.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Import untuk Log
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

public class RegisterFragment extends Fragment {

    private EditText etEmail, etPassword;
    private Button btnRegister, btnGoLogin;
    private FirebaseAuth mAuth;
    private static final String TAG = "RegisterFragment"; // Tag untuk Log

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();

        etEmail = view.findViewById(R.id.et_email_register);
        etPassword = view.findViewById(R.id.et_password_register);
        btnRegister = view.findViewById(R.id.btn_register);
        btnGoLogin = view.findViewById(R.id.btn_go_login);

        btnRegister.setOnClickListener(v -> registerUser());

        btnGoLogin.setOnClickListener(v -> {
            // Kembali ke LoginFragment
            Log.e(TAG, "Navigating back to LoginFragment"); // Log saat tombol login diklik
            getParentFragmentManager()
                    .popBackStack();
        });

        return view;
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String pwd = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email harus diisi");
            Log.e(TAG, "Email field is empty"); // Log error untuk email kosong
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            etPassword.setError("Password harus diisi");
            Log.e(TAG, "Password field is empty"); // Log error untuk password kosong
            return;
        }
        if (pwd.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            Log.e(TAG, "Password is less than 6 characters"); // Log error untuk password pendek
            return;
        }

        Log.e(TAG, "Attempting to register user with email: " + email); // Log sebelum registrasi
        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "User registration successful for email: " + email); // Log saat registrasi berhasil
                        // Setelah register berhasil kembali ke login
                        getParentFragmentManager()
                                .popBackStack();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(getContext(), "Registrasi gagal: " + errorMessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "User registration failed: " + errorMessage); // Log saat registrasi gagal
                    }
                });
    }
}