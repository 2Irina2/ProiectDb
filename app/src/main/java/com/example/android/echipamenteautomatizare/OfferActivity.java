package com.example.android.echipamenteautomatizare;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.echipamenteautomatizare.Adapters.CPUsAdapter;
import com.example.android.echipamenteautomatizare.Objects.CPU;

import java.util.ArrayList;
import java.util.List;

public class OfferActivity extends AppCompatActivity {

    AppDatabase mDb;
    CPUsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        ActionBar actionBar = getSupportActionBar();
        mDb = AppDatabase.getsInstance(this);

        Intent intent = getIntent();
        String query = intent.getStringExtra("query");
        int protocolId = intent.getIntExtra("protocolId", -1);
        int cardId = intent.getIntExtra("cardId", -1);

        List<CPU> cpus = new ArrayList<>();
        if(query != null){
            cpus =  mDb.cpuDao().loadCpusBySpecs(new SimpleSQLiteQuery(query));
            String countQuery = intent.getStringExtra("countQuery");
            String averageQuery = intent.getStringExtra("averageQuery");

            int cpusCount = mDb.cpuDao().countCpus(new SimpleSQLiteQuery(countQuery));
            float avgPrice = mDb.cpuDao().averagePrice(new SimpleSQLiteQuery(averageQuery));
            actionBar.setTitle(Integer.toString(cpusCount) + " CPUs found -- Avg price " + avgPrice);
        } else if(protocolId != -1){
            cpus = mDb.cpuProtocolDao().getCPUsForProtocol(protocolId);
            float average = 0;
            for(CPU cpu : cpus){
                average += cpu.getPrice();
            }
            actionBar.setTitle(Integer.toString(cpus.size()) + " CPUs found -- Avg price " + average / cpus.size());
        } else if(cardId != -1){
            cpus = mDb.cpuCardDao().getCPUsForCardJoin(cardId);
            float average = 0;
            for(CPU cpu : cpus){
                average += cpu.getPrice();
            }
            actionBar.setTitle(Integer.toString(cpus.size()) + " CPUs found -- Avg price " + average / cpus.size());
        }
        setUpRecyclerView(cpus);
    }

    private void setUpRecyclerView(List<CPU> cpus) {
        RecyclerView recyclerView = findViewById(R.id.offer_cpus);
        mAdapter = new CPUsAdapter(this);
        mAdapter.setCPUs(cpus);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
