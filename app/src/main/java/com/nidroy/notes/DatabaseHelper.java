package com.nidroy.notes;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String databaseName = "ScheduleOfClasses.db";
    private static String databasePath = "";
    private static final int databaseVersion = 1;

    private SQLiteDatabase database;
    private final Context context;
    private boolean isDatabaseUpdate = false;

    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, databaseVersion);
        if (Build.VERSION.SDK_INT >= 17)
            databasePath = context.getApplicationInfo().dataDir + "/databases/";
        else
            databasePath = "/data/data/" + context.getPackageName() + "/databases/";
        this.context = context;

        CopyDatabase();

        this.getReadableDatabase();
    }

    // обновление базы данных
    public void UpdateDatabase() throws IOException {
        if (isDatabaseUpdate) {
            File databaseFile = new File(databasePath + databaseName);
            if (databaseFile.exists())
                databaseFile.delete();

            CopyDatabase();

            isDatabaseUpdate = false;
        }
    }

    // открытие базы данных
    public boolean OpenDatabase() throws SQLException {
        database = SQLiteDatabase.openDatabase(databasePath + databaseName, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return database != null;
    }

    // копирование базы данных
    private void CopyDatabase() {
        if (!checkDatabase()) {
            this.getReadableDatabase();
            this.close();
            try {
                CopyDatabaseFile();
            } catch (IOException exception) {
                throw new Error("Error when copying the database");
            }
        }
    }

    // копирование файла базы данных
    private void CopyDatabaseFile() throws IOException {
        InputStream inputStream = context.getAssets().open(databaseName);
        OutputStream outputStream = new FileOutputStream(databasePath + databaseName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0)
            outputStream.write(buffer, 0, length);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    // проверка на существование базы данных
    private boolean checkDatabase() {
        File databaseFile = new File(databasePath + databaseName);
        return databaseFile.exists();
    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            isDatabaseUpdate = true;
    }
}
