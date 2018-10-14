package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "manufacturers")
public class Manufacturer {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String family;

    @Ignore
    public Manufacturer(String name, String family){
        this.name = name;
        this.family = family;
    }

    public Manufacturer(int id, String name, String family){
        this.id = id;
        this.name = name;
        this.family = family;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getFamily() {
        return family;
    }
}
