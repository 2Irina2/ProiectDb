package com.example.android.echipamenteautomatizare.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.echipamenteautomatizare.Adapters.IOOnboardAdapter;
import com.example.android.echipamenteautomatizare.AppDatabase;
import com.example.android.echipamenteautomatizare.MainViewModel;
import com.example.android.echipamenteautomatizare.Objects.IOOnboard;
import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.R;

import java.util.List;

public class IOOnboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView emptyRv;
    private IOOnboardAdapter mAdapter;
    private LayoutInflater mInflater;
    private AppDatabase mDb;

    public IOOnboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getsInstance(getContext());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mInflater = inflater;
        View root = mInflater.inflate(R.layout.fragment_ioonboard, container, false);

        setUpRecyclerView(root);
        setUpFab(root);

        return root;
    }

    private void setUpRecyclerView(View root){
        recyclerView = root.findViewById(R.id.recyclerview_ioonboards);
        emptyRv = root.findViewById(R.id.empty_listview_ioonboards);
        mAdapter = new IOOnboardAdapter(getContext());
        setUpViewModel();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteManufacturer(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setUpFab(View root){
        FloatingActionButton floatingActionButton = root.findViewById(R.id.fab_ioonboards);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final View dialog = mInflater.inflate(R.layout.dialog_ioonboard, null);
                final AlertDialog alertDialog = alert.setView(dialog)
                        .setTitle("Add a IOOnboard to the DB")
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", null)
                        .create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        buttonPositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addIOOnboard(dialog, alertDialog);
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

    private void setUpViewModel(){
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getIOOnboards().observe(this, new Observer<List<IOOnboard>>() {
            @Override
            public void onChanged(@Nullable List<IOOnboard> ioOnboards) {
                mAdapter.setIOOnboards(ioOnboards);
                if(mAdapter.getItemCount() != 0){
                    emptyRv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addIOOnboard(View dialog, AlertDialog alertDialog){
        Spinner nameSpinner = dialog.findViewById(R.id.spinner_ioonboard_name);
        EditText channelsField = dialog.findViewById(R.id.edittext_ioonboard_channels);

        if(channelsField.getText().toString().equals("")){
            channelsField.setError("Must not be empty");
        } else {
            String name = nameSpinner.getSelectedItem().toString();
            int channels = Integer.valueOf(channelsField.getText().toString());

            mDb.ioOnboardDao().insertIOOnboard(new IOOnboard(name, channels));
            alertDialog.cancel();
        }
    }

    private void deleteManufacturer(int position){
        List<IOOnboard> ioOnboards = mAdapter.getIOOnboards();
        mDb.ioOnboardDao().deleteIOOnboard(ioOnboards.get(position));
    }
}
