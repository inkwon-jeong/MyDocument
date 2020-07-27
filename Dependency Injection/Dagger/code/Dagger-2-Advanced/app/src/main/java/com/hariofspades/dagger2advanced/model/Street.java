package com.hariofspades.dagger2advanced.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Street {

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("name")
    @Expose
    private String name;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Street{" +
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}
