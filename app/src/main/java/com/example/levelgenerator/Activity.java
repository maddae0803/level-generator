package com.example.levelgenerator;

/**
 * Created by maxwelladdae0803 on 1/5/17.
 */

public class Activity {
    protected String name;
    protected int prevlevel;
    protected int currentLevel;
    protected String date;

    public Activity() {
        name = "";
        prevlevel = 1;
        currentLevel = 1;
        date = "";
    }
    public boolean equals(Object o) {
        Activity a = (Activity) o;
        return name.equals(a.name);
    }
    public String toString() {
        return name + ": " + currentLevel + " - " + date;
    }

}
