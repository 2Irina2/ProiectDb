package com.example.android.echipamenteautomatizare;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.echipamenteautomatizare.DAOs.CPUCardDao;
import com.example.android.echipamenteautomatizare.DAOs.CPUDao;
import com.example.android.echipamenteautomatizare.DAOs.CPUProtocolDao;
import com.example.android.echipamenteautomatizare.DAOs.CardDao;
import com.example.android.echipamenteautomatizare.DAOs.IOOnboardDao;
import com.example.android.echipamenteautomatizare.DAOs.ManufacturerDao;
import com.example.android.echipamenteautomatizare.DAOs.ProtocolDao;
import com.example.android.echipamenteautomatizare.DAOs.UserDao;
import com.example.android.echipamenteautomatizare.Objects.CPU;
import com.example.android.echipamenteautomatizare.Objects.CPUCard;
import com.example.android.echipamenteautomatizare.Objects.CPUProtocol;
import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.Objects.IOOnboard;
import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.Objects.Protocol;
import com.example.android.echipamenteautomatizare.Objects.User;

@Database(entities = {Manufacturer.class, Protocol.class, Card.class, IOOnboard.class,
        CPU.class, User.class, CPUCard.class, CPUProtocol.class},
        version = 13,
        exportSchema = false)
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
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5,
                                MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_12_13)
                .build();
            }
        }
        Log.d(LOG_TAG, "Getting the Database instance");
        return sInstance;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `protocols` (`id` INTEGER PRIMARY KEY NOT NULL, "
                    + "`name` TEXT NOT NULL, "
                    + "`interf` TEXT NOT NULL, "
                    + "`type` TEXT NOT NULL);");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `cards` (`id` INTEGER PRIMARY KEY NOT NULL, "
                    + "`name` TEXT NOT NULL, "
                    + "`channels` INTEGER NOT NULL, "
                    + "`family` TEXT NOT NULL, "
                    + "`type` TEXT NOT NULL);");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `ioonboards` (`id` INTEGER PRIMARY KEY NOT NULL, "
                    + "`name` TEXT NOT NULL, "
                    + "`channels` INTEGER NOT NULL);");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE `cards`");

            database.execSQL("CREATE TABLE `cards` (`id` INTEGER PRIMARY KEY NOT NULL, "
                    + "`name` TEXT NOT NULL, "
                    + "`channels` INTEGER NOT NULL, "
                    + "`manufacturerId` INTEGER NOT NULL, "
                    + "`type` TEXT NOT NULL, "
                    + "FOREIGN KEY(manufacturerId) REFERENCES manufacturers(id));"
            );
        }
    };

    private static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `users` (`id` INTEGER PRIMARY KEY NOT NULL, " +
                    "`username` TEXT NOT NULL, " +
                    "`password` TEXT NOT NULL);");
        }
    };

    private static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("INSERT INTO users (username, password) VALUES ('admin', 'adminP');");
            database.execSQL("INSERT INTO users (username, password) VALUES ('client', 'clientP');");
        }
    };

    private static final Migration MIGRATION_10_11 = new Migration(10,11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `cpus_cards` (`cpuId` INTEGER NOT NULL, "
                    + "`cardId` INTEGER NOT NULL, "
                    + "PRIMARY KEY(cpuId, cardId), "
                    + "FOREIGN KEY(cpuId) REFERENCES cpus(id), "
                    + "FOREIGN KEY(cardId) REFERENCES cards(id));"
            );
            database.execSQL("CREATE TABLE `cpus_protocols` (`cpuId` INTEGER NOT NULL, "
                    + "`protocolId` INTEGER NOT NULL, "
                    + "PRIMARY KEY(cpuId, protocolId), "
                    + "FOREIGN KEY(cpuId) REFERENCES cpus(id), "
                    + "FOREIGN KEY(protocolId) REFERENCES protocols(id));"
            );
        }
    };

    private static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("INSERT INTO users (username, password) VALUES ('admin', 'adminP');");
            database.execSQL("INSERT INTO users (username, password) VALUES ('client', 'clientP');");
        }
    };

    public abstract ManufacturerDao manufacturerDao();
    public abstract ProtocolDao protocolDao();
    public abstract CardDao cardDao();
    public abstract IOOnboardDao ioOnboardDao();
    public abstract CPUDao cpuDao();
    public abstract UserDao userDao();
    public abstract CPUCardDao cpuCardDao();
    public abstract CPUProtocolDao cpuProtocolDao();
}
