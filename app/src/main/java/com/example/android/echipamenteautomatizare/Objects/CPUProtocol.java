package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "cpus_protocols",
        primaryKeys = { "cpuId", "protocolId"},
        foreignKeys = {
                @ForeignKey(entity = CPU.class,
                            parentColumns = "id",
                            childColumns = "cpuId"),
                @ForeignKey(entity = Protocol.class,
                            parentColumns = "id",
                            childColumns = "protocolId")
        })
public class CPUProtocol {
    @NotNull
    private int cpuId;
    @NotNull
    private int protocolId;

    public CPUProtocol(int cpuId, int protocolId){
        this.cpuId = cpuId;
        this.protocolId = protocolId;
    }

    public int getCpuId() {
        return cpuId;
    }

    public int getProtocolId() {
        return protocolId;
    }

    public void setCpuId(int cpuId) {
        this.cpuId = cpuId;
    }

    public void setProtocolId(int protocolId) {
        this.protocolId = protocolId;
    }
}
