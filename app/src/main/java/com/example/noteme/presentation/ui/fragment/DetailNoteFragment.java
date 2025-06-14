package com.example.noteme.presentation.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.noteme.R;
import com.example.noteme.data.local.NoteDao;
import com.example.noteme.domain.model.Note;

public class DetailNoteFragment extends Fragment {

    private TextView tvTitle, tvContent;
    private Button btnEdit, btnDelete;
    private NoteDao noteDao;
    private Note note;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_note, container, false);

        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        btnEdit = view.findViewById(R.id.btn_edit);
        btnDelete = view.findViewById(R.id.btn_delete);

        noteDao = new NoteDao(getContext());

        // Mendapatkan catatan dari bundle
        if (getArguments() != null) {
            note = (Note) getArguments().getSerializable("note");
            if (note != null) {
                tvTitle.setText(note.getTitle());
                tvContent.setText(note.getContent());
            }
        }

        btnEdit.setOnClickListener(v -> editNote());
        btnDelete.setOnClickListener(v -> deleteNote());

        return view;
    }

    private void editNote() {
        // Implementasikan logika edit catatan di sini
        // Misalnya, buka dialog untuk mengedit catatan
        Toast.makeText(getContext(), "Edit feature is under construction", Toast.LENGTH_SHORT).show();
    }

    private void deleteNote() {
        // Hapus catatan dari database
        noteDao.deleteNote(note.getId());
        Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();

        // Kembali ke halaman sebelumnya
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
