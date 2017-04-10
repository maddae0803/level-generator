package com.example.levelgenerator;


import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.SelectRequest;

import java.util.ArrayList;
import java.util.List;

public class ReadFromDB extends AsyncTask<Void, Void, Participant[]> {

    List<com.amazonaws.services.simpledb.model.Item> list = null; //Mathlete List
    List<com.amazonaws.services.simpledb.model.Item> list2 = null; //Coach List
    List<com.amazonaws.services.simpledb.model.Item> successList = null;

    public AmazonSimpleDB getAWSSimpleDB() {
        AmazonSimpleDB ret = new AmazonSimpleDBClient(AWSCredentialsHolder.fractions_game);
        ret.setEndpoint("sdb.amazonaws.com"); //time-zone, for some reason is needed, probably for datetime??
        return ret;
    }

    //reads and combines Mathlete and Coach Lists
    public List<Participant> readDatabase() {
        List<Participant> ret = new ArrayList<Participant>();
        SelectRequest selectRequest=  new SelectRequest("select * from dubois_mathlete_identities limit 1000").withConsistentRead(true);
        list = getAWSSimpleDB().select(selectRequest).getItems();


        for (int x = 0; x < list.size(); x++) {
            com.amazonaws.services.simpledb.model.Item temp1= list.get(x); //Mathlete Row

            List<com.amazonaws.services.simpledb.model.Attribute> tempAttributes = temp1.getAttributes();
            Mathlete m = new Mathlete();
            for (int y = 0; y < tempAttributes.size(); y++) {
                String attributeName = tempAttributes.get(y).getName();
                if (attributeName.equals("name")) {
                    m.setName(tempAttributes.get(y).getValue());
                }
                else if (attributeName.equals("last_name")) {
                    m.setName(m.getName() + " " + tempAttributes.get(y).getValue());
                }
                else if (attributeName.startsWith("level")) {
                    Activity a = new Activity();
                    String[] split = attributeName.split("_");
                    a.name = split[1];

                    if(tempAttributes.get(y).getValue().equals("") == false)
                        a.currentLevel = Integer.parseInt(tempAttributes.get(y).getValue());

                    m.activities.add(a);
                }

            }
            m.setID(temp1.getName());
            /**
            String query = "'" + m.getID() + "'";
            SelectRequest successRequest = new SelectRequest("select * from dubois_mathlete_successes limit 1000").withConsistentRead(true);
            successList = getAWSSimpleDB().select(successRequest).getItems();

            for (int pos = 0; pos < successList.size(); pos++) {
                com.amazonaws.services.simpledb.model.Item successRow = successList.get(pos);
                List<com.amazonaws.services.simpledb.model.Attribute> successAttributes = successRow.getAttributes();
                Attribute attr = new Attribute().withName("Mathlete_id").withValue(m.getID());
                Activity a = new Activity();
                if (successAttributes.contains(attr)) {

                    for (int p = 0; p < successAttributes.size(); p++) {
                        String attributeName = successAttributes.get(p).getName();
                        if (attributeName.equals("Activity"))
                            a.name = successAttributes.get(p).getValue();
                        else if (attributeName.equals("DateTime"))
                            a.date = successAttributes.get(p).getValue();
                        else if (attributeName.equals("New_Level"))
                            a.currentLevel = Integer.parseInt(successAttributes.get(p).getValue());
                        else if (attributeName.equals("Prev_Level"))
                            a.prevlevel = Integer.parseInt(successAttributes.get(p).getValue());

                    }
                    if (m.containsActivity(a.name) == false)
                        m.activities.add(a);

                    if (m.successes.get(a.name) == null)
                        m.successes.put(a.name, a);
                }
            }**/
            ret.add(m);
        }

        SelectRequest selectRequest1 = new SelectRequest("select * from dubois_coach_identities").withConsistentRead(true);
        list2 = getAWSSimpleDB().select(selectRequest1).getItems();

        for (int z = 0; z < list2.size(); z++) {
            com.amazonaws.services.simpledb.model.Item CoachRow = list2.get(z);
            List<com.amazonaws.services.simpledb.model.Attribute> coachAttributes = CoachRow.getAttributes();
            Coach c = new Coach();
            for (Attribute a: coachAttributes) {
                if (a.getName().equals("name")) {
                    c.setName(a.getValue());
                }
            }
            c.setID(CoachRow.getName());
            ret.add(c);
        }

        return ret;
    }

    @Override
    protected Participant[] doInBackground(Void... params) {
        // TODO Auto-generated method stub
        try {
            return readDatabase().toArray(new Participant[readDatabase().size()]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    }
