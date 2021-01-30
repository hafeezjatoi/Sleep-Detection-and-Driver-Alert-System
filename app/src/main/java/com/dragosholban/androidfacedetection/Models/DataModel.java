package com.dragosholban.androidfacedetection.Models;

public class DataModel
{
    private String number;
    private int id;

    public DataModel(int id, String number)
    {
        this.id = id;
        this.number = number;
    }

    public String GetNumber()
    {
        return number;
    }
    public int GetID(){ return id; }
}
