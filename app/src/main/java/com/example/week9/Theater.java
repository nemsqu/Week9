package com.example.week9;

public class Theater {

    String id;
    String theaterName;

    Theater(String ID, String name){
        id = ID;
        theaterName = name;
    }

    public String getID(){
        return id;
    }

    public String getTheaterName(){
        return theaterName;
    }

    public String toString(){
        return theaterName;

    }
}
