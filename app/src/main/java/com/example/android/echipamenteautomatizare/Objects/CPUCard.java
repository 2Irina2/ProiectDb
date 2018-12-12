package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "cpus_cards",
        primaryKeys = { "cpuId", "cardId" },
        foreignKeys = {
                @ForeignKey(entity = CPU.class,
                            parentColumns = "id",
                            childColumns = "cpuId"),
                @ForeignKey(entity = Card.class,
                            parentColumns = "id",
                            childColumns = "cardId")
        })
public class CPUCard {
    @NotNull
    private long cpuId;
    @NotNull
    private int cardId;

    public CPUCard(long cpuId, int cardId){
        this.cpuId = cpuId;
        this.cardId = cardId;
    }

    public long getCpuId() {
        return cpuId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCpuId(int cpuId) {
        this.cpuId = cpuId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}
