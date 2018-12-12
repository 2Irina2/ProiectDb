package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import org.jetbrains.annotations.NotNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "cpus_protocols",
        primaryKeys = { "cpuId", "protocolId"},
        foreignKeys = {
                @ForeignKey(entity = CPU.class,
                            parentColumns = "id",
                            childColumns = "cpuId",
                            onDelete = CASCADE),
                @ForeignKey(entity = Protocol.class,
                            parentColumns = "id",
                            childColumns = "protocolId",
                            onDelete = CASCADE)
        })
public class CPUProtocol {
    @NotNull
    private long cpuId;
    @NotNull
    private int protocolId;

    public CPUProtocol(long cpuId, int protocolId){
        this.cpuId = cpuId;
        this.protocolId = protocolId;
    }

    public long getCpuId() {
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
