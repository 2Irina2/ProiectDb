package com.example.android.echipamenteautomatizare.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.android.echipamenteautomatizare.AppDatabase;
import com.example.android.echipamenteautomatizare.MainViewModel;
import com.example.android.echipamenteautomatizare.Objects.CPU;
import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.Objects.IOOnboard;
import com.example.android.echipamenteautomatizare.R;

import java.util.ArrayList;
import java.util.List;

public class CpusFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyRv;
    private CPUsAdapter mAdapter;
    private LayoutInflater mInflater;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        View root = mInflater.inflate(R.layout.fragment_cpus, container, false);

        setUpRecyclerView(root);
        setUpFab(root);

        return root;
    }

    private void setUpRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.recyclerview_cpus);
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
                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                final View dialog = mInflater.inflate(R.layout.dialog_cpu, null);
                final AlertDialog alertDialog = alert.setView(dialog)
                        .setTitle("Add a cpu to the DB")
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", null)
                        .create();

                final Spinner ioSpinner = dialog.findViewById(R.id.spinner_cpu_io);
                mDb.ioOnboardDao().loadAllIOOnboards().observe(getActivity(), new Observer<List<IOOnboard>>() {
                    @Override
                    public void onChanged(@Nullable List<IOOnboard> ioonboards) {
                        List<String> ioOnboardsLabels = new ArrayList<>();
                        if(ioonboards != null){
                            for(IOOnboard io:ioonboards){
                                ioOnboardsLabels.add(String.valueOf(io.getChannels()) + io.getName());
                            }
                        }
                        ArrayAdapter<String> ioSpinnerArrayAdapter = new ArrayAdapter<String>(
                                mContext, android.R.layout.simple_spinner_item, ioOnboardsLabels);
                        ioSpinner.setAdapter(ioSpinnerArrayAdapter);
                    }
                });

                List<String> families = mDb.manufacturerDao().loadAllFamilies();
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                        mContext, android.R.layout.simple_spinner_item, families);
                final Spinner familySpinner = dialog.findViewById(R.id.spinner_cpu_manufacturer);
                familySpinner.setAdapter(spinnerArrayAdapter);

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        buttonPositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addCpu(dialog, alertDialog, ioSpinner, familySpinner);
                            }
                        });

                        Button buttonNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        buttonNegative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.cancel();
                            }
                        });
                    }
                });

                alertDialog.show();
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

    private void addCpu(View dialog, AlertDialog alertDialog, Spinner ioSpinner, Spinner familySpinner) {
        EditText nameField = dialog.findViewById(R.id.edittext_cpu_name);
        EditText memoryField = dialog.findViewById(R.id.edittext_cpu_memory);
        EditText supplyField = dialog.findViewById(R.id.edittext_cpu_supply);
        EditText priceField = dialog.findViewById(R.id.edittext_cpu_price);
        EditText codeField = dialog.findViewById(R.id.edittext_cpu_code);
        List<Integer> ioIds = mDb.ioOnboardDao().loadIds();

        String name = nameField.getText().toString();
        int memory = memoryField.getText().toString().isEmpty() ? -1 : Integer.valueOf(memoryField.getText().toString());
        float supply = supplyField.getText().toString().isEmpty() ? -1 : Float.valueOf(supplyField.getText().toString());
        float price = priceField.getText().toString().isEmpty() ? -1 : Float.valueOf(priceField.getText().toString());
        int code = codeField.getText().toString().isEmpty() ? -1 : Integer.valueOf(codeField.getText().toString());
        int ioId = ioIds.get(ioSpinner.getSelectedItemPosition());
        int manufacturerId = mDb.manufacturerDao().
                loadManufacturerForFamily(familySpinner.getSelectedItem().toString());

        boolean notNull = true;
        if(name.isEmpty() && memory == -1 && supply == -1 && price == -1 && code == -1){
            alertDialog.cancel();
            Toast.makeText(mContext, "No item was added to database", Toast.LENGTH_SHORT).show();
            return;
        }
        if(name.isEmpty()){
            nameField.setError("Must not be empty");
            notNull = false;
        }
        if(memory == -1){
            memoryField.setError("Must not be empty");
            notNull = false;
        }
        if(supply == -1){
            supplyField.setError("Must not be empty");
            notNull = false;
        }
        if(price == -1){
            priceField.setError("Must not be empty");
            notNull = false;
        }
        if(code == -1){
            codeField.setError("Must not be empty");
            notNull = false;
        }
        if(notNull){
            CPU cpu = new CPU(name, memory, 0, code, supply, price, manufacturerId, ioId);
            mDb.cpuDao().insertCpu(cpu);
            alertDialog.cancel();
        }
    }

    private void deleteCpu(int position) {
        List<CPU> cpus = mAdapter.getCpus();
        mDb.cpuDao().deleteCpu(cpus.get(position));
    }
}
