package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.echipamenteautomatizare.Objects.Manufacturer;

import java.util.List;

@Dao
public interface ManufacturerDao {

    @Query("SELECT * FROM manufacturers")
    LiveData<List<Manufacturer>> loadAllManufacturers();

    @Insert
    void insertManufacturer(Manufacturer manufacturer);

    @Delete
    void deleteManufacturer(Manufacturer manufacturer);
}
