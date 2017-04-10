package com.example.levelgenerator;

/**
 * Created by maxwelladdae0803 on 1/25/17.
 */

import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class ReadFromSuccessDB extends AsyncTask<Mathlete, Void, Void> {
    protected final String input = ScanMathlete.input;
    protected final AmazonSimpleDB database = new ReadFromDB().getAWSSimpleDB();
    List<Item> successList = null;


    public void storeSuccesses(Mathlete m) {
        SelectRequest successRequest = new SelectRequest("select * from dubois_mathlete_successes limit 1000").withConsistentRead(true);
        successList = database.select(successRequest).getItems();

        for (int pos = 0; pos < successList.size(); pos++) {
            com.amazonaws.services.simpledb.model.Item successRow = successList.get(pos);
            List<com.amazonaws.services.simpledb.model.Attribute> successAttributes = successRow.getAttributes();
            String dataID = successAttributes.get(4).getValue();
            Activity a = new Activity();
            if (dataID.equals(input)) {

                DateTime dataDate;
                DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

                for (int p = 0; p < successAttributes.size(); p++) {
                    String attributeName = successAttributes.get(p).getName();
                    if (attributeName.equals("Activity"))
                        a.name = successAttributes.get(p).getValue();
                    else if (attributeName.equals("DateTime")) {
                        dataDate = format.parseDateTime(successAttributes.get(p).getValue());
                        a.date = dataDate.toString();
                    }
                    else if (attributeName.equals("New_Level"))
                        a.currentLevel = Integer.parseInt(successAttributes.get(p).getValue());
                    else if (attributeName.equals("Prev_Level"))
                        a.prevlevel = Integer.parseInt(successAttributes.get(p).getValue());

                }
                if (m.containsActivity(a.name) == false)
                    m.activities.add(a);
                if (m.successes.get(a.name) == null)
                    m.successes.put(a.name, a);
                else if (format.parseDateTime(m.successes.get(a.name).date).isBefore(format.parseDateTime(a.date))) {
                    m.successes.put(a.name, a);
                    replace(m.activities, a);
                }
            }
        }

    }
    private void replace(List<Activity> list, Activity a) {
        for (Activity activity: list) {
            if (activity.name.equals(a.name)) {
                activity.prevlevel = a.prevlevel;
                activity.currentLevel = a.currentLevel;
                activity.date = a.date;
                return;
            }
        }
    }

    @Override
    protected Void doInBackground(Mathlete... params) {
        storeSuccesses(params[0]);
        System.out.println(params[0].successes);
        System.out.println(params[0].activities);
        return null;
    }
}
//#779b20c0-676c-4d5a-96a7-3a9f1f54d07e