package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "ioonboards")
public class IOOnboard {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    private int channels;

    @Ignore
    public IOOnboard(@NonNull String name, int channels) {
        this.name = name;
        this.channels = channels;
    }

    public IOOnboard(int id, @NonNull String name, int channels) {
        this.id = id;
        this.name = name;
        this.channels = channels;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getChannels() {
        return channels;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }
}
