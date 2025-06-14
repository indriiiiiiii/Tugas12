package com.example.noteme.presentation.ui.fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.noteme.R;
import com.example.noteme.data.local.NoteDao;
import com.example.noteme.domain.model.Note;
import com.example.noteme.presentation.ui.notification.NotificationReceiver;

import java.io.File;
import java.util.Calendar;

public class AddNoteDialogFragment extends DialogFragment {

    private EditText etTitle, etContent;
    private TextView tvSelectedDate;
    private Button btnAddImage, btnSelectDate, btnSaveNote;
    private Uri imageUri;
    private long selectedDate;
    private NoteDao noteDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_note, container, false);

        etTitle = view.findViewById(R.id.et_title);
        etContent = view.findViewById(R.id.et_content);
        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        btnAddImage = view.findViewById(R.id.btn_add_image);
        btnSelectDate = view.findViewById(R.id.btn_select_date);
        btnSaveNote = view.findViewById(R.id.btn_save_note);

        noteDao = new NoteDao(getContext());

        btnAddImage.setOnClickListener(v -> openCamera());
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSaveNote.setOnClickListener(v -> saveNote());

        return view;
    }


    private void openCamera() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1); // Untuk memilih gambar
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedDate = calendar.getTimeInMillis();
                    tvSelectedDate.setText("Notifikasi pada: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "Judul dan konten harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simpan ke database
        Note note = new Note(0, title, content, selectedDate);
        noteDao.insertNote(note);

        if (imageUri != null) {
            // Simpan gambar jika ada
        }

        // Jadwalkan notifikasi
        scheduleNotification(selectedDate);

        dismiss();
        Toast.makeText(getContext(), "Catatan berhasil disimpan", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == getActivity().RESULT_OK && requestCode == 1 && data != null) {
//            imageUri = data.getData();
//        }
//    }

    private void scheduleNotification(long notificationTime) {
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Menjadwalkan notifikasi pada waktu yang ditentukan
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
        }
    }


}
