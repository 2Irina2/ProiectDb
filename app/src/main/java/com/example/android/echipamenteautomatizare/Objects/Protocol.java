package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "protocols")
public class Protocol {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String interf;
    private int type;

    @Ignore
    public Protocol(String name, String interf, int type){
        this.name = name;
        this.interf = interf;
        this.type = type;
    }

    public Protocol(int id, String name, String interf, int type){
        this.id = id;
        this.name = name;
        this.interf = interf;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInterf() {
        return interf;
    }

    public int getType() {
        return type;
    }
}
