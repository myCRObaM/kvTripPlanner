package com.example.kvtripplanner.Classes;

public class Item {
    private String name;
    private String value;

    public Item(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Item()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
