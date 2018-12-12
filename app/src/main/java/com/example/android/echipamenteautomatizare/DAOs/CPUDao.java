package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import com.example.android.echipamenteautomatizare.Objects.CPU;

import java.util.List;

@Dao
public interface CPUDao {
    @Query("SELECT * FROM cpus")
    LiveData<List<CPU>> loadAllCpusLive();

    @Query("SELECT * FROM cpus")
    List<CPU> loadAllCpus();

    @Query("SELECT * FROM cpus WHERE id=:cpuId")
    CPU loadCpu(long cpuId);

    @Query("SELECT id FROM cpus WHERE name=:cpuName")
    int loadCpuByName(String cpuName);

    @RawQuery
    List<CPU> loadCpusBySpecs(SimpleSQLiteQuery query);

    @RawQuery
    int countCpus(SimpleSQLiteQuery query);

    @RawQuery
    float averagePrice(SimpleSQLiteQuery query);

    @Insert
    long insertCpu(CPU cpu);

    @Delete
    void deleteCpu(CPU cpu);

}
