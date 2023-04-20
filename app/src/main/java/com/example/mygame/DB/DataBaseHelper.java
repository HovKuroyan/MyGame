package com.example.mygame.DB;

import static com.example.mygame.DB.MyDatabase.COLUMN_GAME_MODE;
import static com.example.mygame.DB.MyDatabase.COLUMN_HIGH_SCORE;
import static com.example.mygame.DB.MyDatabase.COLUMN_ID;
import static com.example.mygame.DB.MyDatabase.COLUMN_NAME;
import static com.example.mygame.DB.MyDatabase.COLUMN_SURNAME;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper {

    private MyDatabase myDatabase;

    public DataBaseHelper(Context context) {
        myDatabase = new MyDatabase(context);
    }

    public List<Result> getResults() {
        List<Result> resultList = new ArrayList<>();

        Cursor cursor = myDatabase.getAllResults();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String surname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURNAME));
            int highScore = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HIGH_SCORE));
            String gameMode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAME_MODE));

            Result result = new Result(id, name, surname, highScore, gameMode);
            resultList.add(result);
        }
        cursor.close();

        return resultList;
    }

    public void insertResult(Result result) {
        myDatabase.insertResult(result);
    }

    public void updateResult(Result result) {
        myDatabase.updateResult(result);
    }

    public void deleteResult(int id) {
        myDatabase.deleteResult(id);
    }
}
