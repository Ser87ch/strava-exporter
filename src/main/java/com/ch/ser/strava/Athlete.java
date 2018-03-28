package com.ch.ser.strava;

import org.json.JSONObject;

public class Athlete {

    private final int id;
    private final String firstName;
    private final String secondName;
    private String mileage;

    public Athlete(JSONObject jsonObject) {
        this(jsonObject.getInt("id"), jsonObject.getString("firstname"), jsonObject.getString("lastname"));
    }

    public Athlete(int id, String firstName, String secondName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Athlete athlete = (Athlete) o;

        return id == athlete.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", firstName, secondName, mileage);
    }
}
