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
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.echipamenteautomatizare.AppDatabase;
import com.example.android.echipamenteautomatizare.MainViewModel;
import com.example.android.echipamenteautomatizare.Adapters.ManufacturersAdapter;
import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.R;

import java.util.List;

public class ManufacturersFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyRv;
    private ManufacturersAdapter mAdapter;
    private LayoutInflater mInflater;
    private AppDatabase mDb;

    public ManufacturersFragment() {
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
        View root = mInflater.inflate(R.layout.fragment_manufacturers, container, false);

        setUpRecyclerView(root);
        setUpFab(root);

        return root;
    }

    private void setUpRecyclerView(View root){
        recyclerView = root.findViewById(R.id.recyclerview_manufacturers);
        emptyRv = root.findViewById(R.id.empty_listview_manufacturers);
        mAdapter = new ManufacturersAdapter(getContext());
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
        FloatingActionButton floatingActionButton = root.findViewById(R.id.fab_manufacturers);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final View dialog = mInflater.inflate(R.layout.dialog_manufacturer, null);
                final AlertDialog alertDialog = alert.setView(dialog)
                        .setTitle("Add a manufacturer to the DB")
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
                                addManufacturer(dialog, alertDialog);
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
        mainViewModel.getManufacturers().observe(this, new Observer<List<Manufacturer>>() {
            @Override
            public void onChanged(@Nullable List<Manufacturer> manufacturers) {
                mAdapter.setManufacturers(manufacturers);
                if(mAdapter.getItemCount() != 0){
                    emptyRv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addManufacturer(View dialog, AlertDialog alertDialog){
        EditText nameField = dialog.findViewById(R.id.edittext_manufacturer_name);
        EditText familyField = dialog.findViewById(R.id.edittext_manufacturer_family);

        String name = nameField.getText().toString();
        String family = familyField.getText().toString();

        if(name.equals("")){
            if(family.equals("")){
                alertDialog.cancel();
                Toast.makeText(getContext(), "No item was added to database", Toast.LENGTH_SHORT).show();
                return;
            }
            nameField.setError("Must not be empty");
        } else if(family.equals("")){
            familyField.setError("Must not be empty");
        } else {
            mDb.manufacturerDao().insertManufacturer(new Manufacturer(name, family));
            alertDialog.cancel();
        }
    }

    private void deleteManufacturer(int position){
        List<Manufacturer> manufacturers = mAdapter.getManufacturers();
        mDb.manufacturerDao().deleteManufacturer(manufacturers.get(position));
    }
}
