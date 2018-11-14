package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.Objects.IOOnboard;

import java.util.List;

@Dao
public interface IOOnboardDao {
    @Query("SELECT * FROM ioonboards")
    LiveData<List<IOOnboard>> loadAllIOOnboards();

    @Query("SELECT channels FROM ioonboards WHERE id=:id")
    int loadChannelsForIOOnboard(final int id);

    @Query("SELECT name FROM ioonboards WHERE id=:id")
    String loadNameForIOOnboard(final int id);

    @Query("SELECT id FROM ioonboards")
    List<Integer> loadIds();

    @Insert
    void insertIOOnboard(IOOnboard ioOnboard);

    @Delete
    void deleteIOOnboard(IOOnboard ioOnboard);
}
