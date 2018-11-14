package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "cards",
        foreignKeys = @ForeignKey(entity = Manufacturer.class,
                                  parentColumns = "id",
                                  childColumns = "manufacturerId",
                                  onDelete = CASCADE))
public class Card {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    private int channels;
    @NonNull
    private int manufacturerId;
    @NonNull
    private String type;

    @Ignore
    public Card(@NonNull String name, int channels, @NonNull int manufacturerId, @NonNull String type){
        this.name = name;
        this.channels = channels;
        this.manufacturerId = manufacturerId;
        this.type = type;
    }

    public Card(int id, @NonNull String name, int channels, @NonNull int manufacturerId, @NonNull String type){
        this.id = id;
        this.name = name;
        this.channels = channels;
        this.manufacturerId = manufacturerId;
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
    public int getManufacturerId() {
        return manufacturerId;
    }

    @NonNull
    public String getType() {
        return type;
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

    public void setManufacturerId(@NonNull int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }
}

