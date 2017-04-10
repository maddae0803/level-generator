package com.example.levelgenerator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class DisplayLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_level);

        Bundle extras = getIntent().getExtras();
        String selection = extras.getString("selection");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTextSize(40);
        Mathlete m = (Mathlete) MainActivity.map.get(ScanMathlete.input);
        String message = m.getName() + " should be given Level ";
        for (Activity a: m.activities) {
            if (a.name.equals(selection)) {
                message += a.currentLevel;
                break;
            }
        }
        message += " on the " + selection + " activity";
        Spannable WordtoSpan = new SpannableString(message);
        int startLoc = message.indexOf("Level");
        int endLoc = message.indexOf("on");
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), startLoc, endLoc,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(WordtoSpan);
    }
}
