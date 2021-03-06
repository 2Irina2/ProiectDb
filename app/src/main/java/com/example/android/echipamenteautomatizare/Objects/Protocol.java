package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "protocols")
public class Protocol {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String interf;
    @NonNull
    private String type;

    @Ignore
    public Protocol(@NonNull String name, @NonNull String interf, @NonNull String type){
        this.name = name;
        this.interf = interf;
        this.type = type;
    }

    public Protocol(int id, @NonNull String name, @NonNull String interf, @NonNull String type){
        this.id = id;
        this.name = name;
        this.interf = interf;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getInterf() {
        return interf;
    }

    @NonNull
    public String getType() {
        return type;
    }
}
