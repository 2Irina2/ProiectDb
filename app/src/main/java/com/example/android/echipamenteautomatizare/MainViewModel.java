package com.example.android.echipamenteautomatizare;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.Objects.Protocol;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Manufacturer>> manufacturers;
    private LiveData<List<Protocol>> protocols;

    public MainViewModel(@NonNull Application application){
        super(application);
        AppDatabase appDatabase = AppDatabase.getsInstance(this.getApplication());
        manufacturers = appDatabase.manufacturerDao().loadAllManufacturers();
        protocols = appDatabase.protocolDao().loadAllProtocols();
    }

    public LiveData<List<Manufacturer>> getManufacturers() {
        return manufacturers;
    }
    public LiveData<List<Protocol>> getProtocols() {
        return protocols;
    }
}
