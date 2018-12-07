package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT password FROM users WHERE username=:username")
    String loadPasswordForUsername(final String username);

    @Query("SELECT username FROM users")
    List<String> loadAllUsers();

    @Query("SELECT password FROM users")
    List<String> loadAllPasswords();
}
