package com.example.levelgenerator;

/**
 * Created by maxwelladdae0803 on 1/5/17.
 */

public class Coach implements Participant {
    private String id;
    private String name;

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
}
