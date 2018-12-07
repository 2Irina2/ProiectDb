package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.echipamenteautomatizare.Objects.CPU;
import com.example.android.echipamenteautomatizare.Objects.CPUCard;
import com.example.android.echipamenteautomatizare.Objects.Card;

import java.util.List;

@Dao
public interface CPUCardDao {
    @Insert
    void insert(CPUCard cpuCard);

    @Query("SELECT * FROM cpus INNER JOIN cpus_cards ON cpus.id=cpus_cards.cpuId " +
            "WHERE cpus_cards.cardId=:cardId")
    List<CPU> getCPUsForCard(int cardId);

    @Query("SELECT * FROM cards INNER JOIN cpus_cards ON cards.id=cpus_cards.cardId " +
            "WHERE cpus_cards.cpuId=:cpuId")
    List<Card> getCardsForCPU(int cpuId);
}
