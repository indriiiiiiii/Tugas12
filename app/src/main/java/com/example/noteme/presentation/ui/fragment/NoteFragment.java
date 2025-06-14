package com.example.noteme.presentation.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteme.R;
import com.example.noteme.data.local.NoteDao;
import com.example.noteme.domain.model.Note;
import com.example.noteme.presentation.ui.adapter.NoteAdapter;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private NoteDao noteDao;
    private Handler handler = new Handler();
    private Executor executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteDao = new NoteDao(getContext());

        loadNotes();

        return view;
    }

    private void loadNotes() {
        // Load data dari SQLite di background thread, update UI di main thread
        executor.execute(() -> {
            List<Note> notes = noteDao.getAllNotes();
            handler.post(() -> adapter.setNotes(notes));
        });
    }
}
