package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.echipamenteautomatizare.Objects.Protocol;

import java.util.List;

@Dao
public interface ProtocolDao {
    @Query("SELECT * FROM protocols")
    LiveData<List<Protocol>> loadAllProtocols();

    @Insert
    void insertProtocol(Protocol protocol);

    @Delete
    void deleteProtocol(Protocol protocol);
}
