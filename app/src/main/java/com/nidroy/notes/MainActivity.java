package com.nidroy.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // переменные для работы с БД
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        try {
            databaseHelper.UpdateDatabase();
        } catch (IOException exception) {
            throw new Error("Failed to update database");
        }
        try {
            database = databaseHelper.getWritableDatabase();
        } catch (SQLException exception) {
            throw exception;
        }
    }

    // выполнить запрос с ответом в виде таблицы
    private List<List<String>> ExecuteQueryWithAnswer(String query) {

        List<List<String>> table = new ArrayList<List<String>>();

        Cursor cursor = database.rawQuery(query, null);
        int columnCount = cursor.getColumnCount();

        for (int i = 0; i < columnCount; i++) {
            List<String> cells = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cells.add(cursor.getString(i));
                cursor.moveToNext();
            }
            table.add(cells);
        }

        return table;
    }
}