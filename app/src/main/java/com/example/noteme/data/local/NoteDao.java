package com.example.noteme.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.noteme.domain.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDao {
    private NoteDatabase dbHelper;

    public NoteDao(Context context) {
        dbHelper = NoteDatabase.getInstance(context);
    }

    public long insertNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteDatabase.COLUMN_TITLE, note.getTitle());
        values.put(NoteDatabase.COLUMN_CONTENT, note.getContent());
        values.put(NoteDatabase.COLUMN_CREATED_AT, note.getCreatedAt());

        long id = db.insert(NoteDatabase.TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(NoteDatabase.TABLE_NOTES,
                null, null, null, null, null, NoteDatabase.COLUMN_CREATED_AT + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(NoteDatabase.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COLUMN_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COLUMN_CONTENT));
                long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(NoteDatabase.COLUMN_CREATED_AT));

                notes.add(new Note(id, title, content, createdAt));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return notes;
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(NoteDatabase.TABLE_NOTES, NoteDatabase.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
