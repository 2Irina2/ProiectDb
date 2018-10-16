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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.echipamenteautomatizare.Adapters.CardsAdapter;
import com.example.android.echipamenteautomatizare.AppDatabase;
import com.example.android.echipamenteautomatizare.MainViewModel;
import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.Objects.Protocol;
import com.example.android.echipamenteautomatizare.R;

import java.util.List;

public class CardsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyRv;
    private CardsAdapter mAdapter;
    private LayoutInflater mInflater;
    private AppDatabase mDb;

    public CardsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getsInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        View root = mInflater.inflate(R.layout.fragment_cards, container, false);

        setUpRecyclerView(root);
        setUpFab(root);

        return root;
    }

    private void setUpRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.recyclerview_cards);
        emptyRv = root.findViewById(R.id.empty_listview_cards);
        mAdapter = new CardsAdapter(getContext());
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
                deleteCard(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setUpFab(View root) {
        FloatingActionButton floatingActionButton = root.findViewById(R.id.fab_protocols);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final View dialog = mInflater.inflate(R.layout.dialog_card, null);
                final AlertDialog alertDialog = alert.setView(dialog)
                        .setTitle("Add a card to the DB")
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
                                addCard(dialog, alertDialog);
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
        mainViewModel.getCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                mAdapter.setCards(cards);
                if(mAdapter.getItemCount() != 0){
                    emptyRv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addCard(View dialog, AlertDialog alertDialog){
        Spinner nameSpinner = dialog.findViewById(R.id.spinner_card_name);
        EditText channelsField = dialog.findViewById(R.id.edittext_card_channels);
        Spinner familySpinner = dialog.findViewById(R.id.spinner_card_family);
        EditText typeField = dialog.findViewById(R.id.edittext_card_type);

        String name = nameSpinner.getSelectedItem().toString();
        int channels = Integer.valueOf(channelsField.getText().toString());
        String family = familySpinner.getSelectedItem().toString();
        String type = typeField.getText().toString();

        if(channelsField.getText().toString().equals("")){
            if(type.equals("")){
                alertDialog.cancel();
                Toast.makeText(getContext(), "No item was added to database", Toast.LENGTH_SHORT).show();
                return;
            }
            channelsField.setError("Must not be empty");
        } else if(type.equals("")){
            typeField.setError("Must not be empty");
        } else {
            mDb.cardDao().insertCard(new Card(name, channels, family, type));
            alertDialog.cancel();
        }
    }

    private void deleteCard(int position){
        List<Card> cards = mAdapter.getCards();
        mDb.cardDao().deleteCard(cards.get(position));
    }
}
