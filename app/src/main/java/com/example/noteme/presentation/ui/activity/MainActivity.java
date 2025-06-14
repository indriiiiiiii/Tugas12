package com.example.noteme.presentation.ui.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.noteme.R;
import com.example.noteme.presentation.ui.fragment.AddNoteDialogFragment;
import com.example.noteme.presentation.ui.fragment.LoginFragment;
import com.example.noteme.presentation.ui.fragment.NoteFragment;
import com.example.noteme.presentation.ui.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Terapkan tema
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        Button btnAddNote = findViewById(R.id.btn_add_note);

        // Jika pengguna sudah login, tampilkan NoteFragment dan BottomNavigationView
        if (isUserLoggedIn()) {
            showNoteFragment();
        } else {
            showLoginFragment();
        }

        // Tombol untuk membuka dialog AddNote
        btnAddNote.setOnClickListener(v -> {
            AddNoteDialogFragment addNoteDialog = new AddNoteDialogFragment();
            addNoteDialog.show(getSupportFragmentManager(), "AddNoteDialog");
        });

        // Setup bottom navigation listener
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_notes) {
                selectedFragment = new NoteFragment();
                // Tampilkan tombol saat berada di NoteFragment
                btnAddNote.setVisibility(View.VISIBLE);
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                // Sembunyikan tombol saat berada di ProfileFragment
                btnAddNote.setVisibility(View.GONE);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Buat Channel untuk notifikasi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Note Notifications";
            String description = "Channel for note notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("noteChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    private void showLoginFragment() {
        // Menampilkan LoginFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();

        // Menyembunyikan BottomNavigationView
        bottomNav.setVisibility(View.GONE);
    }

    private void showNoteFragment() {
        // Menampilkan NoteFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new NoteFragment())
                .commit();

        // Menampilkan BottomNavigationView setelah login berhasil
        bottomNav.setVisibility(View.VISIBLE);
    }
}
