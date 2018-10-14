package com.example.android.echipamenteautomatizare;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.echipamenteautomatizare.Objects.Manufacturer;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Manufacturer>> manufacturers;

    public MainViewModel(@NonNull Application application){
        super(application);
        AppDatabase appDatabase = AppDatabase.getsInstance(this.getApplication());
        manufacturers = appDatabase.manufacturerDao().loadAllManufacturers();
    }

    public LiveData<List<Manufacturer>> getManufacturers() {
        return manufacturers;
    }
}
