package com.example.mygame.flags;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mygame.DB.DataBaseHelper;
import com.example.mygame.DB.Result;
import com.example.mygame.MenuActivity;
import com.example.mygame.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FlagsActivity extends Activity {
    String[] countries, europe, flagUrls;
    int[] nextRandQuest;
    Button cBtn1, cBtn2, cBtn3,
            cancelBtn, enterBtn;
    String answerCountry;
    private static int answerPos, length;
    private static int points;
    Random random = new Random();
    private static int percent;
    DataBaseHelper dbHelper;
    View saveScoreView, blackView;
    private List<Result> res;
    private String gameMode = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flags);
        cBtn1 = findViewById(R.id.cBtn1);
        cBtn2 = findViewById(R.id.cBtn2);
        cBtn3 = findViewById(R.id.cBtn3);
        dbHelper = new DataBaseHelper(this);
        saveScoreView = findViewById(R.id.my_view);
        cancelBtn = findViewById(R.id.cancelBtn);
        enterBtn = findViewById(R.id.enterBtn);
        blackView = findViewById(R.id.blackView);

        cBtn1.setClickable(false);
        cBtn2.setClickable(false);
        cBtn3.setClickable(false);

        blackView.getBackground().setAlpha(100);
        blackView.setVisibility(View.INVISIBLE);
        blackView.setClickable(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        enterBtn.setWidth(width / 2);
        cancelBtn.setWidth(width / 2);

        cancelBtn.setBackgroundColor(Color.parseColor("#f7f7f7"));
        cancelBtn.setTextColor(Color.parseColor("#454545"));

        enterBtn.setBackgroundColor(Color.parseColor("#0000ff"));

        saveScoreView.setVisibility(View.INVISIBLE);


        res = dbHelper.getResults();


        flagUrls = new String[250]; // Set the size of the array to 250, assuming that's the number of flags on the website
        new FlagScraperTask().execute();
        countries = new String[]{
                "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia",
                "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus",
                "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil",
                "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Côte d'Ivoire", "Cabo Verde", "Cambodia", "Cameroon", "Canada",
                "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo, Democratic Republic of the",
                "Congo", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic",
                "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor (see Timor-Leste)", "Ecuador", "Egypt",
                "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini (formerly Swaziland)", "Ethiopia", "Fiji",
                "Finland", "France", "Gabon", "Gambia, The", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala",
                "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran",
                "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati",
                "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania",
                "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania",
                "Mauritius", "Mexico", "Micronesia, Federated States of", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco",
                "Mozambique", "Myanmar (formerly Burma)", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua",
                "Niger", "Nigeria", "North Macedonia", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay",
                "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
                "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore",
                "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka",
                "St. Vincent Grenadines", "State of Palestine", "Sudan", "Suriname", "Sweden", "Switzerland", "Syria", "Tajikistan", "Tanzania",
                "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "United Arab Emirates",
                "United Kingdom", "United States of America", "Uganda", "Ukraine", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Yemen",
                "Zambia", "Zimbabwe"
        };

        europe = new String[]{
                "Albania", "Andorra", "Austria", "Belarus", "Belgium", "Bosnia and Herzegovina", "Bulgaria", "Croatia",
                "Cyprus", "Czech Republic", "Denmark", "Estonia", "Finland", "France", "Germany", "Greece",
                "Hungary", "Iceland", "Ireland", "Italy", "Latvia", "Liechtenstein", "Lithuania",
                "Luxembourg", "Malta", "Moldova", "Monaco", "Montenegro", "Netherlands", "North Macedonia",
                "Norway", "Poland", "Portugal", "Romania", "Russia", "San Marino", "Serbia", "Slovakia",
                "Slovenia", "Spain", "Sweden", "Switzerland", "Ukraine", "United Kingdom"
        };

        if (res.get(0).getGameMode().equals("all")) {
            gameMode = "all";
            for (int i = 0; i < countries.length; i++) {
                europe[i] = countries[i];
            }
        } else if (res.get(0).getGameMode().equals("europe")) {
            gameMode = "europe";
        }
        answerPos = 0;
        points = 0;

        try {
//            flags.add(0,getFlagsURLs.getFlagUrls().get(0));
            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }


    public void onCBtnClick(View view) {
        Button b = (Button) view;
        String buttonText = b.getText().toString();


        if (buttonText == answerCountry) {
            ++points;
        } else {
            changeBtnColor(b, 'r');
        }

        if (cBtn1.getText().toString() == answerCountry) {
            changeBtnColor(cBtn1, 'g');
        } else if (cBtn2.getText().toString() == answerCountry) {
            changeBtnColor(cBtn2, 'g');
        } else {
            changeBtnColor(cBtn3, 'g');
        }
    }

    private void questOrBack(char color) {
        if (color == 'g') {
            if (answerPos == length) {
                if (isRecord(points)) {
                    slideUp(saveScoreView);
                    blackView.setVisibility(View.VISIBLE);
                    enterBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                setPoints();
                            } catch (RuntimeException ignored) {
                            }
                            finish();
                            overridePendingTransition(R.anim.slide_in_left,
                                    R.anim.slide_out_right);
                        }
                    });
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            overridePendingTransition(R.anim.slide_in_left,
                                    R.anim.slide_out_right);
                        }
                    });


                } else startActivity(new Intent(this, MenuActivity.class));

//                overridePendingTransition(R.anim.slide_in_right,
//                        R.anim.slide_out_left);
            } else {

                newQuest(europe, flagUrls, cBtn1, cBtn2, cBtn3);

            }
        }
    }

    void startMenuActivity() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    private void changeBtnColor(Button button, char color) {
        if (color == 'g') {
            button.setBackgroundColor(Color.GREEN);
        } else {
            button.setBackgroundColor(Color.RED);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setBackgroundColor(Color.BLUE);
                questOrBack(color);
            }
        }, 500);
    }

    private void newQuest(String[] countries, String[] flags, Button cBtn1, Button cBtn2, Button cBtn3) {
        cBtn1.setClickable(true);
        cBtn2.setClickable(true);
        cBtn3.setClickable(true);

        String[] variants = new String[3];

        //get random positions
        int cPos2 = random.nextInt(length);
        int cPos3 = random.nextInt(length);
        while (cPos2 == answerPos || cPos3 == cPos2) {
            cPos2 = random.nextInt(length);
        }
        while (cPos3 == cPos2 || cPos3 == answerPos) {
            cPos3 = random.nextInt(length);
        }

        //get variants names
        answerCountry = countries[nextRandQuest[answerPos]];
        String cName1 = countries[nextRandQuest[cPos2]];
        String cName2 = countries[nextRandQuest[cPos3]];

        //random set variants places
        variants[random.nextInt(3)] = answerCountry;
        for (int i = 0; i < 3; i++) {
            if (variants[i] == answerCountry) {
                if (i == 0) {
                    variants[1] = cName1;
                    variants[2] = cName2;
                } else if (i == 1) {
                    variants[0] = cName1;
                    variants[2] = cName2;
                } else {
                    variants[0] = cName1;
                    variants[1] = cName2;
                }
                break;
            }
        }
        //set names
        cBtn1.setText(variants[0]);
        cBtn2.setText(variants[1]);
        cBtn3.setText(variants[2]);


        int t = 0;
        //set flag
        if (flagUrls != null) {
            for (int i = 0; i < this.countries.length; i++) {
                if (this.countries[i].equals(countries[nextRandQuest[answerPos]])) {
                    t = i;
                }
            }

            String answerFlag = flagUrls[t];

            ImageView img = findViewById(R.id.flags);
            Picasso.with(this).load(answerFlag).into(img);
            ++answerPos;
        }

    }

    private class FlagScraperTask extends AsyncTask<Void, Void, String[]> {
        String TAG = "FlagScraper";

        @Override
        protected String[] doInBackground(Void... voids) {
            try {
                String url = "https://www.worldometers.info/geography/flags-of-the-world/";
                Document doc = Jsoup.connect(url).get();

                Elements imgElements = doc.select("img[src*=/flags/]");
                for (int i = 0; i < imgElements.size(); i++) {
                    Element imgElement = imgElements.get(i);
                    flagUrls[i] = imgElement.attr("src");
                }

            } catch (IOException e) {
                Log.e(TAG, "Error scraping flag URLs", e);
            }
            return flagUrls;
        }

        @Override
        protected void onPostExecute(String[] flagUrls) {
            //getting urls
            for (int i = 0; i < flagUrls.length; i++) {
                if (flagUrls[i] != null) {
                    flagUrls[i] = "https://www.worldometers.info/" + flagUrls[i];
                    String wordToRemove = "small/tn_";
                    String newString = flagUrls[i].replaceAll(wordToRemove, "");
                    flagUrls[i] = newString;
                }
            }


            // Display or process the flag URLs here
            length = europe.length;
            nextRandQuest = new int[length];
            for (int i = 0; i < length; i++) {
                nextRandQuest[i] = i;
            }
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(i + 1);
                int a = nextRandQuest[index];
                nextRandQuest[index] = nextRandQuest[i];
                nextRandQuest[i] = a;
            }

            newQuest(europe, flagUrls, cBtn1, cBtn2, cBtn3);
            cBtn1.setBackgroundColor(Color.BLUE);
            cBtn2.setBackgroundColor(Color.BLUE);
            cBtn3.setBackgroundColor(Color.BLUE);

            for (int i = 0; i < length; i++) {
                Log.d(TAG, countries[i] + " Flag URL: " + flagUrls[i]);
            }
        }


    }

    private static int[] arr = new int[11];

    private boolean isRecord(int points) {
        percent = points * 100 / countries.length;
        res = dbHelper.getResults();
        Arrays.fill(arr, 0);
        arr[10] = percent;

        try {
            for (int i = 0; i < res.size(); i++) {
                arr[i] = res.get(i).getHighScore();
            }
        } catch (RuntimeException ignored) {
        }

        //array operations
        Arrays.sort(arr);
        reverse(arr, arr.length);

        if (arr[10] != percent) {
            return true;
        } else return false;
    }

    private void setPoints() {
        res = dbHelper.getResults();
        try {
            for (int i = 0; i < 10; i++) {
                Result result = new Result(i + 1, res.get(0).getName(), res.get(0).getSurname(), arr[i], res.get(0).getGameMode());
                dbHelper.updateResult(result);
            }

        } catch (RuntimeException ignored) {
        }
    }


    static void reverse(int[] a, int n) {
        int i, t;
        for (i = 0; i < n / 2; i++) {
            t = a[i];
            a[i] = a[n - i - 1];
            a[n - i - 1] = t;
        }
    }

    //slide operations
    private void slideUp(View view) {
        cBtn1.setClickable(false);
        cBtn2.setClickable(false);
        cBtn3.setClickable(false);

        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(100);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}
