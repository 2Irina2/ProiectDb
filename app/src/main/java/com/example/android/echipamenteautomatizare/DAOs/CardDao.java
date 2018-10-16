package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.echipamenteautomatizare.Objects.Card;

import java.util.List;

@Dao
public interface CardDao {
    @Query("SELECT * FROM cards")
    LiveData<List<Card>> loadAllCards();

    @Insert
    void insertCard(Card card);

    @Delete
    void deleteCard(Card card);
}
