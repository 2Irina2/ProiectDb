package com.example.android.echipamenteautomatizare.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.echipamenteautomatizare.AddComponentActivity;
import com.example.android.echipamenteautomatizare.AdminActivity;
import com.example.android.echipamenteautomatizare.AppDatabase;
import com.example.android.echipamenteautomatizare.MainViewModel;
import com.example.android.echipamenteautomatizare.Adapters.ManufacturersAdapter;
import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.R;

import java.util.List;

public class ManufacturersFragment extends Fragment {

    private TextView emptyRv;
    private ManufacturersAdapter mAdapter;
    private AppDatabase mDb;

    public ManufacturersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getsInstance(getContext());
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_manufacturers, container, false);

        setUpRecyclerView(root);
        setUpFab(root);

        return root;
    }

    private void setUpRecyclerView(View root){
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_manufacturers);
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
                startActivity(new Intent(getActivity(), AddComponentActivity.class)
                        .putExtra("component", AdminActivity.MANUFACTURERS_FRAGMENT));
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

    private void deleteManufacturer(int position){
        List<Manufacturer> manufacturers = mAdapter.getManufacturers();
        mDb.manufacturerDao().deleteManufacturer(manufacturers.get(position));
        manufacturers.remove(position);
        if (manufacturers.isEmpty()){
            emptyRv.setVisibility(View.VISIBLE);
        }
    }
}
