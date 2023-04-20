package com.example.mygame.flags;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygame.DB.DataBaseHelper;
import com.example.mygame.DB.Result;
import com.example.mygame.R;

import java.util.List;

public class FlagsScore extends AppCompatActivity {
    DataBaseHelper dbHelper;
    int[] top10;
    TextView score1, score2, score3, score4, score5, score6, score7, score8, score9, score10;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flags_score);
        top10 = new int[10];

        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        score3 = findViewById(R.id.score3);
        score4 = findViewById(R.id.score4);
        score5 = findViewById(R.id.score5);
        score6 = findViewById(R.id.score6);
        score7 = findViewById(R.id.score7);
        score8 = findViewById(R.id.score8);
        score9 = findViewById(R.id.score9);
        score10 = findViewById(R.id.score10);

        dbHelper = new DataBaseHelper(this);
//        for (int i = 2; i < 11; i++) {
//            Result result = new Result(i, "Hovo", "Kuroyan", 0);
//            dbHelper.insertResult(result);
//        }
        List<Result> res = dbHelper.getResults();

        try {
            for (int i = 0; i < 10; i++) {
                    top10[i] = res.get(i).getHighScore();
            }
        } catch (RuntimeException ignored) {
        }

        score1.setText(top10[0] + "%");
        score2.setText(top10[1] + "%");
        score3.setText(top10[2] + "%");
        score4.setText(top10[3] + "%");
        score5.setText(top10[4] + "%");
        score6.setText(top10[5] + "%");
        score7.setText(top10[6] + "%");
        score8.setText(top10[7] + "%");
        score9.setText(top10[8] + "%");
        score10.setText(top10[9] + "%");
    }

    public void onFlagsPlayClick(View view) {
        Intent in = new Intent(this, FlagsActivity.class);
        startActivity(in);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}