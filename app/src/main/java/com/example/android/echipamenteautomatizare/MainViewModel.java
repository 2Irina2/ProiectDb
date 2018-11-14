package com.example.android.echipamenteautomatizare;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.echipamenteautomatizare.Objects.CPU;
import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.Objects.IOOnboard;
import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.Objects.Protocol;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Manufacturer>> manufacturers;
    private LiveData<List<Protocol>> protocols;
    private LiveData<List<Card>> cards;
    private LiveData<List<IOOnboard>> ioOnboards;
    private LiveData<List<CPU>> cpus;

    public MainViewModel(@NonNull Application application){
        super(application);
        AppDatabase appDatabase = AppDatabase.getsInstance(this.getApplication());
        manufacturers = appDatabase.manufacturerDao().loadAllManufacturers();
        protocols = appDatabase.protocolDao().loadAllProtocols();
        cards = appDatabase.cardDao().loadAllCards();
        ioOnboards = appDatabase.ioOnboardDao().loadAllIOOnboards();
        cpus = appDatabase.cpuDao().loadAllCpus();
    }

    public LiveData<List<Manufacturer>> getManufacturers() {
        return manufacturers;
    }
    public LiveData<List<Protocol>> getProtocols() {
        return protocols;
    }
    public LiveData<List<Card>> getCards(){
        return cards;
    }
    public LiveData<List<IOOnboard>> getIOOnboards(){
        return ioOnboards;
    }
    public LiveData<List<CPU>> getCpus() {
        return cpus;
    }
}
