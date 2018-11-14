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

    @Query("SELECT family FROM manufacturers")
    List<String> loadAllFamilies();

    @Query("SELECT id FROM manufacturers WHERE family=:family")
    int loadManufacturerForFamily(final String family);

    @Query("SELECT family FROM manufacturers WHERE id=:manId")
    String loadFamilyForManufacturer(final int manId);

    @Insert
    void insertManufacturer(Manufacturer manufacturer);

    @Delete
    void deleteManufacturer(Manufacturer manufacturer);
}
