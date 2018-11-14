package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "cpus",
        foreignKeys = {
                @ForeignKey(entity = Manufacturer.class,
                        parentColumns = "id",
                        childColumns = "manufacturerId",
                        onDelete = CASCADE),
                @ForeignKey(entity = IOOnboard.class,
                        parentColumns = "id",
                        childColumns = "ioonboardId",
                        onDelete = CASCADE)})
public class CPU {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    @NonNull
    private int memory;
    @NonNull
    private int temperature;
    @NonNull
    private int code;
    @NotNull
    private float supply;
    @NotNull
    private float price;
    @NotNull
    private int manufacturerId;
    @NotNull
    private int ioonboardId;

    @Ignore
    public CPU(@NotNull String name, int memory, int temperature, int code, float supply,
               float price, int manufacturerId, int ioonboardId) {
        this.name = name;
        this.memory = memory;
        this.temperature = temperature;
        this.code = code;
        this.supply = supply;
        this.price = price;
        this.manufacturerId = manufacturerId;
        this.ioonboardId = ioonboardId;
    }

    public CPU(int id, @NotNull String name, int memory, int temperature, int code, float supply,
               float price, int manufacturerId, int ioonboardId) {
        this.id = id;
        this.name = name;
        this.memory = memory;
        this.temperature = temperature;
        this.code = code;
        this.supply = supply;
        this.price = price;
        this.manufacturerId = manufacturerId;
        this.ioonboardId = ioonboardId;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public int getMemory() {
        return memory;
    }

    @NonNull
    public int getTemperature() {
        return temperature;
    }

    @NonNull
    public int getCode() {
        return code;
    }

    @NotNull
    public float getSupply() {
        return supply;
    }

    @NotNull
    public float getPrice() {
        return price;
    }

    @NotNull
    public int getManufacturerId() {
        return manufacturerId;
    }

    @NotNull
    public int getIoonboardId() {
        return ioonboardId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setMemory(@NonNull int memory) {
        this.memory = memory;
    }

    public void setTemperature(@NonNull int temperature) {
        this.temperature = temperature;
    }

    public void setCode(@NonNull int code) {
        this.code = code;
    }

    public void setSupply(@NotNull float supply) {
        this.supply = supply;
    }

    public void setPrice(@NotNull float price) {
        this.price = price;
    }

    public void setManufacturerId(@NotNull int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public void setIoonboardId(@NotNull int ioonboardId) {
        this.ioonboardId = ioonboardId;
    }
}
