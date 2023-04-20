package com.example.mygame.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "results.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_RESULTS = "results";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_HIGH_SCORE = "high_score";
    public static final String COLUMN_GAME_MODE = "game_mode";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESULTS_TABLE = "CREATE TABLE " + TABLE_RESULTS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT," +
                COLUMN_SURNAME + " TEXT," +
                COLUMN_HIGH_SCORE + " INTEGER," +
                COLUMN_GAME_MODE + " INTEGER" +
                ")";
        db.execSQL(CREATE_RESULTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
        onCreate(db);
    }

    public void insertResult(Result result) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, result.getName());
        values.put(COLUMN_SURNAME, result.getSurname());
        values.put(COLUMN_HIGH_SCORE, result.getHighScore());
        values.put(COLUMN_GAME_MODE, result.getGameMode());

        db.insert(TABLE_RESULTS, null, values);
        db.close();
    }

    public void updateResult(Result result) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, result.getName());
        values.put(COLUMN_SURNAME, result.getSurname());
        values.put(COLUMN_HIGH_SCORE, result.getHighScore());
        values.put(COLUMN_GAME_MODE, result.getGameMode());

        db.update(TABLE_RESULTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(result.getId())});

        db.close();
    }

    public void deleteResult(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_RESULTS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getAllResults() {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_SURNAME,
                COLUMN_HIGH_SCORE,
                COLUMN_GAME_MODE
        };

        return db.query(
                TABLE_RESULTS,
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }
}
