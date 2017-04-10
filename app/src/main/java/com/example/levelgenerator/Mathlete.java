package com.example.levelgenerator;

/**
 * Created by maxwelladdae0803 on 1/5/17.
 */
import java.util.*;


public class Mathlete implements Participant {
    private String id;
    private String name;
    protected List<Activity> activities = new ArrayList<Activity>();
    protected HashMap<String, Activity> successes = new HashMap<>();

    @Override
    public void setID(String s) {
        id = s;
    }

    @Override
    public void setName(String s) {
        name = s;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean containsActivity(String name) {
        for (Activity a: activities) {
            if (a.name.equals(name))
                return true;
        }
        return false;
    }
}
