package com.example.mygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygame.DB.DataBaseHelper;
import com.example.mygame.DB.Result;
import com.example.mygame.flags.FlagsScore;

import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private ProgressBar progressBarF;
    private TextView flagProgressTxt;
    DataBaseHelper dbHelper;
    int flagsRecord;


    private static String gameMode = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        progressBarF = findViewById(R.id.progressBarFlag);
        flagProgressTxt = findViewById(R.id.percentFlag);

        dbHelper = new DataBaseHelper(this);
        List<Result> r = dbHelper.getResults();
        flagsRecord = r.get(0).getHighScore();

        //set progress
        progressBarF.setProgress(flagsRecord);
        flagProgressTxt.setText(setPercent(flagsRecord));


    }

    private String setPercent(int percent) {
        return percent + "%";
    }

    public void onLayClick(View view) {
        //TODO create this part of game
        //Intent intent = new Intent(this, GameActivity.class);
        //startActivity(intent);
        Toast.makeText(this, "Not working", Toast.LENGTH_SHORT).show();
    }

    public void onFlagClick(View view) {
        if (view.getId() == R.id.allFlags) {
            gameMode = "all";
        } else if (view.getId() == R.id.flagsEurope) {
            gameMode = "europe";
        }
        startActivity(new Intent(this, FlagsScore.class));
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    public static String getGameMode() {
        return gameMode;
    }
}