package com.example.android.echipamenteautomatizare.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
@Dao
public interface UserDao {

    @Query("SELECT password FROM users WHERE username=:username")
    String loadPasswordForUsername(final String username);
}
