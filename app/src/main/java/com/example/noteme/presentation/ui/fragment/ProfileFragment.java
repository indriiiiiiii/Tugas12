package com.example.noteme.presentation.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.noteme.R;
import com.example.noteme.presentation.ui.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private TextView tvUserEmail;
    private Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUserEmail = view.findViewById(R.id.tv_user_email);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Menampilkan email pengguna
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        tvUserEmail.setText(userEmail);

        // Tombol logout
        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();

        // Hapus status login dari SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", false);
        editor.apply();

        // Kembali ke halaman login
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish(); // Menutup aktivitas ini
    }
}
