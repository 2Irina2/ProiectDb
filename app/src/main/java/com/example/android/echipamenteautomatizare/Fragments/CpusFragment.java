package com.example.android.echipamenteautomatizare.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.echipamenteautomatizare.Adapters.CPUsAdapter;
import com.example.android.echipamenteautomatizare.Adapters.CardsAdapter;
import com.example.android.echipamenteautomatizare.AddComponentActivity;
import com.example.android.echipamenteautomatizare.AdminActivity;
import com.example.android.echipamenteautomatizare.AppDatabase;
import com.example.android.echipamenteautomatizare.MainViewModel;
import com.example.android.echipamenteautomatizare.Objects.CPU;
import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.Objects.IOOnboard;
import com.example.android.echipamenteautomatizare.R;

import java.util.ArrayList;
import java.util.List;

public class CpusFragment extends Fragment {

    private TextView emptyRv;
    private CPUsAdapter mAdapter;
    private AppDatabase mDb;
    private Context mContext;

    public CpusFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getsInstance(getContext());
        mContext = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cpus, container, false);

        setUpRecyclerView(root);
        setUpFab(root);

        return root;
    }

    private void setUpRecyclerView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_cpus);
        emptyRv = root.findViewById(R.id.empty_listview_cpus);
        mAdapter = new CPUsAdapter(mContext);
        setUpViewModel();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteCpu(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setUpFab(View root) {
        FloatingActionButton floatingActionButton = root.findViewById(R.id.fab_cpus);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddComponentActivity.class)
                        .putExtra("component", AdminActivity.CPU_FRAGMENT));
            }
        });
    }

    private void setUpViewModel() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getCpus().observe(this, new Observer<List<CPU>>() {
            @Override
            public void onChanged(@Nullable List<CPU> cpus) {
                mAdapter.setCards(cpus);
                if (mAdapter.getItemCount() != 0) {
                    emptyRv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void deleteCpu(int position) {
        List<CPU> cpus = mAdapter.getCpus();
        mDb.cpuDao().deleteCpu(cpus.get(position));
        cpus.remove(position);
        if(cpus.isEmpty()){
            emptyRv.setVisibility(View.VISIBLE);
        }
    }
}
