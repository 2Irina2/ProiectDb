package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.echipamenteautomatizare.Objects.CPU;
import com.example.android.echipamenteautomatizare.Objects.CPUProtocol;
import com.example.android.echipamenteautomatizare.Objects.IOOnboard;
import com.example.android.echipamenteautomatizare.Objects.Protocol;

import java.util.List;

@Dao
public interface CPUProtocolDao {
    @Insert
    void insert(CPUProtocol cpuProtocol);

    @Query("SELECT * FROM cpus INNER JOIN cpus_protocols ON cpus.id=cpus_protocols.cpuId " +
            "WHERE cpus_protocols.protocolId=:protocolId")
    List<CPU> getCPUsForProtocol(int protocolId);

    @Query("SELECT * FROM protocols INNER JOIN cpus_protocols ON protocols.id=cpus_protocols.protocolId " +
            "WHERE cpus_protocols.cpuId=:cpuId")
    LiveData<List<Protocol>> getProtocolsForCPU(long cpuId);

    @Query("DELETE FROM cpus_protocols WHERE cpuId=:cId AND protocolId=:pId")
    void deleteCPUProtocol(long cId, int pId);

    @Query("SELECT * FROM protocols WHERE protocols.id IN (SELECT cpus_protocols.protocolId FROM cpus_protocols " +
            "WHERE cpus_protocols.cpuId=:cpuId)")
    List<Protocol> getProtocolsForCPUSelect(long cpuId);
}
