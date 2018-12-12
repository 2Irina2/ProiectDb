package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.lifecycle.LiveData;
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
    List<CPU> getCPUsForCardJoin(int cardId);

    @Query("SELECT * FROM cards INNER JOIN cpus_cards ON cards.id=cpus_cards.cardId " +
            "WHERE cpus_cards.cpuId=:cpuId")
    LiveData<List<Card>> getCardsForCPUJoin(long cpuId);

    @Query("DELETE FROM cpus_cards WHERE cpuId=:cId AND cardId=:cardId")
    void deleteCPUCard(long cId, int cardId);

    @Query("SELECT * FROM cards WHERE cards.id IN (SELECT cpus_cards.cardId FROM cpus_cards " +
            "WHERE cpus_cards.cpuId=:cpuId)")
    List<Card> getCardsForCPUSelect(long cpuId);
}
