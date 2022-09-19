package com.nidroy.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // переменные для работы с БД
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    // переменные компонентов верстки
    Button button;
    TextView textView;

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

        // определление переменных через компоненты в XML разметки
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        // обработчик нажатия на кнопку
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "";

                Cursor cursor = database.rawQuery("SELECT * FROM Class_name", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    data += cursor.getString(1) + " | ";
                    cursor.moveToNext();
                }

                textView.setText(data);
            }
        });
    }
}