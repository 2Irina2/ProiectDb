package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.echipamenteautomatizare.Objects.CPU;

import java.util.List;
@Dao
public interface CPUDao {
    @Query("SELECT * FROM cpus")
    LiveData<List<CPU>> loadAllCpus();

    @Insert
    void insertCpu(CPU cpu);

    @Delete
    void deleteCpu(CPU cpu);
}
