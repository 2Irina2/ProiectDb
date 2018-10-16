package com.example.android.echipamenteautomatizare;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

import com.example.android.echipamenteautomatizare.DAOs.ManufacturerDao;
import com.example.android.echipamenteautomatizare.DAOs.ProtocolDao;
import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.Objects.Protocol;

@Database(entities = {Manufacturer.class, Protocol.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "echipamente";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating a new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .addMigrations(MIGRATION_1_2)
                .build();
            }
        }
        Log.d(LOG_TAG, "Getting the Database instance");
        return sInstance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `protocols` (`id` INTEGER PRIMARY KEY NOT NULL, "
                    + "`name` TEXT NOT NULL, "
                    + "`interf` TEXT NOT NULL, "
                    + "`type` TEXT NOT NULL);");
        }
    };

    public abstract ManufacturerDao manufacturerDao();
    public abstract ProtocolDao protocolDao();
}
