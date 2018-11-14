package com.example.android.echipamenteautomatizare.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NotNull
    private String username;
    @NotNull
    private String password;

    @Ignore
    public User(@NotNull String username, @NotNull String password){
        this.username = username;
        this.password = password;
    }

    public User(int id, @NotNull String username, @NotNull String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }
}
