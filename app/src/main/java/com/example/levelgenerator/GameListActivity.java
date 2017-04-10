package com.example.levelgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.concurrent.ExecutionException;

public class GameListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Spinner dropdown = (Spinner)findViewById(R.id.spinner); //dropdown menu
        Bundle extras = getIntent().getExtras();

        Mathlete m = (Mathlete) MainActivity.map.get(ScanMathlete.input);
        try {
            new ReadFromSuccessDB().execute(m).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String[] items = new String[m.activities.size()];

        for (int x = 0; x < m.activities.size(); x++){
            items[x] = m.activities.get(x).name;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, DisplayLevelActivity.class);
        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String activityName = dropdown.getSelectedItem().toString();
        intent.putExtra("selection", activityName);
        startActivity(intent);
    }

}
