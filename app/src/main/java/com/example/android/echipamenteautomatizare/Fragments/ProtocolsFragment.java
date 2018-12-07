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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.echipamenteautomatizare.Adapters.ProtocolsAdapter;
import com.example.android.echipamenteautomatizare.AddComponentActivity;
import com.example.android.echipamenteautomatizare.AdminActivity;
import com.example.android.echipamenteautomatizare.AppDatabase;
import com.example.android.echipamenteautomatizare.MainViewModel;
import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.Objects.Protocol;
import com.example.android.echipamenteautomatizare.R;

import java.util.List;

public class ProtocolsFragment extends Fragment {

    private TextView emptyRv;
    private ProtocolsAdapter mAdapter;
    private AppDatabase mDb;

    public ProtocolsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getsInstance(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_protocols, container, false);

        setUpRecyclerView(root);
        setUpFab(root);

        return root;
    }

    private void setUpRecyclerView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_protocols);
        emptyRv = root.findViewById(R.id.empty_listview_protocols);
        mAdapter = new ProtocolsAdapter(getContext());
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
                deleteProtocol(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setUpFab(View root) {
        FloatingActionButton floatingActionButton = root.findViewById(R.id.fab_protocols);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddComponentActivity.class)
                        .putExtra("component", AdminActivity.PROTOCOLS_FRAGMENT));
            }
        });
    }

    private void setUpViewModel(){
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getProtocols().observe(this, new Observer<List<Protocol>>() {
            @Override
            public void onChanged(@Nullable List<Protocol> protocols) {
                mAdapter.setProtocols(protocols);
                if(mAdapter.getItemCount() != 0){
                    emptyRv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void deleteProtocol(int position){
        List<Protocol> protocols = mAdapter.getProtocols();
        mDb.protocolDao().deleteProtocol(protocols.get(position));
        protocols.remove(position);
        if (protocols.isEmpty()){
            emptyRv.setVisibility(View.VISIBLE);
        }
    }
}
