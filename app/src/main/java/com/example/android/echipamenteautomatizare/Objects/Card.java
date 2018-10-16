package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "cards")
public class Card {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    private int channels;
    @NonNull
    private String family;
    @NonNull
    private String type;

    @Ignore
    public Card(@NonNull String name, int channels, @NonNull String family, @NonNull String type){
        this.name = name;
        this.channels = channels;
        this.family = family;
        this.type = type;
    }

    @Ignore
    public Card(int id, @NonNull String name, int channels, @NonNull String family, @NonNull String type){
        this.id = id;
        this.name = name;
        this.channels = channels;
        this.family = family;
        this.type = type;
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

    @NonNull
    public String getFamily() {
        return family;
    }

    @NonNull
    public String getType() {
        return type;
    }
}

