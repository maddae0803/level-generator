package com.example.levelgenerator;

/**
 * Created by maxwelladdae0803 on 1/19/17.
 */
import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

import java.util.ArrayList;
import java.util.List;

public class StoreToDB extends AsyncTask<ArrayList<String>, Void, Void>{
    AmazonSimpleDB database;
    AmazonSimpleDBClient client;
    /**
    public void logNewData(String itemName, String name) {
        client = new AmazonSimpleDBClient(AWSCredentialsHolder.fractions_game);
        List<ReplaceableAttribute> attribute = new ArrayList<ReplaceableAttribute>();
        attribute.add(new ReplaceableAttribute("name", name, true));
        client.putAttributes(new PutAttributesRequest("dubois_activities", itemName, attribute));
    }**/

    public void logNewSuccess(String itemName, String id, int prevLevel, int newLevel, String activity, String datetime) {
        database = new ReadFromDB().getAWSSimpleDB();
        client = new AmazonSimpleDBClient(AWSCredentialsHolder.fractions_game);
        try {
            //database.createDomain(new CreateDomainRequest("dubois_mathlete_successes"));
            //client.createDomain(new CreateDomainRequest("dubois_mathlete_successes"));
            List<ReplaceableAttribute> attribute = new ArrayList<ReplaceableAttribute>();
            attribute.add(new ReplaceableAttribute("Mathlete_id", id, true));
            attribute.add(new ReplaceableAttribute("New_Level", new Integer(newLevel).toString(), true));
            attribute.add(new ReplaceableAttribute("Prev_Level", new Integer(prevLevel).toString(), true));
            attribute.add(new ReplaceableAttribute("Activity", activity, true));
            attribute.add(new ReplaceableAttribute("DateTime", datetime, true));
            database.putAttributes(new PutAttributesRequest("dubois_mathlete_successes",itemName, attribute));

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /**
    @Override
    protected Void doInBackground(ArrayList<String>... params) {
        logNewData(params[0].get(0), params[0].get(1));
        return null;
    }
**/

    @Override
    protected Void doInBackground(ArrayList<String>... params) {
        logNewSuccess(params[0].get(0), params[0].get(1), Integer.parseInt(params[0].get(2)), Integer.parseInt(params[0].get(3)), params[0].get(4), params[0].get(5));
        return null;
    }

}
