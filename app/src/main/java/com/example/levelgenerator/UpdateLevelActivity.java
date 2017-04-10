package com.example.levelgenerator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class UpdateLevelActivity extends AppCompatActivity {
    private Mathlete m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_level);
        Spinner actDropdown= (Spinner)findViewById(R.id.spinner3); //dropdown menu

        String input = ScanMathlete.input;

        m = (Mathlete) MainActivity.map.get(input);
        try {
            new ReadFromSuccessDB().execute(m).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String[] items = new String[100];
        Spinner activities = (Spinner) findViewById(R.id.spinner2);
        String[] items2 = new String[] {"Chocolate Fix", "KenKen", "Fraction Circle Challenges", "Rush Hour", "BoxBlox", "Multiply Madness", "Multiplication Challenge"};
        for (int x = 1; x <= 100; x++)
            items[x-1] = new Integer(x).toString();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        actDropdown.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        activities.setAdapter(adapter1);

    }

    public void onClick(View view) {
        ArrayList<String> items = new ArrayList<>();
        items.add(UUID.randomUUID().toString()); //itemName
        items.add(m.getID()); //Mathlete ID
        Spinner activity = (Spinner)findViewById(R.id.spinner2);
        Spinner level = (Spinner)findViewById(R.id.spinner3);
        String activityName = activity.getSelectedItem().toString();
        String newLevel = level.getSelectedItem().toString();
        items.add(new Integer(updateLevel(activityName, newLevel)).toString()); //Mathlete's old level
        items.add(newLevel); //mathlete's new level
        items.add(activityName); //activity name
        items.add(new DateTime().toString()); //datetime
        Activity a = new Activity();
        if (getActivity(activityName) != null) {
            a = getActivity(activityName);
            a.date = new DateTime().toString();
        }
        else {
            a.name = activityName;
            a.prevlevel = 1;
            a.currentLevel = new Integer(newLevel);
            a.date = new DateTime().toString();
            m.activities.add(a);
        }
        m.successes.put(activityName, a);
        try {
            new StoreToDB().execute(items).get();
            Toast.makeText(getApplicationContext(), "New Entry Added", Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
        }

    }
    private Activity getActivity(String name) {
        for (Activity a: m.activities) {
            if (a.name.equals(name))
                return a;
        }
        return null;
    }
    //updates currentLevel on activity, returns old level
    private int updateLevel(String activity, String newLevel) {
        for (Activity a: m.activities) {
            if (a.name.equals(activity)) {
                a.prevlevel = a.currentLevel;
                a.currentLevel = Integer.parseInt(newLevel);
               return a.prevlevel;
            }
        }
        return 0;
    }
}
