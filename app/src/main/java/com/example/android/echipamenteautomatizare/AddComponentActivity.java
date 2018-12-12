package com.example.android.echipamenteautomatizare;

import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.echipamenteautomatizare.Adapters.CardsProtocolsAdapter;
import com.example.android.echipamenteautomatizare.Fragments.CardsFragment;
import com.example.android.echipamenteautomatizare.Objects.CPU;
import com.example.android.echipamenteautomatizare.Objects.CPUCard;
import com.example.android.echipamenteautomatizare.Objects.CPUProtocol;
import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.Objects.IOOnboard;
import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.Objects.Protocol;

import java.util.ArrayList;
import java.util.List;

public class AddComponentActivity extends AppCompatActivity {

    ActionBar mActionBar;
    AppDatabase mDb;
    int mComponentType;
    long cpuId = -1;

    Spinner familySpinner_Cpu;
    Spinner familySpinner_Card;
    Spinner ioSpinner_Cpu;
    TextView emptyProtocols;
    TextView emptyCards;
    RecyclerView protocolsRecyclerView;
    RecyclerView cardsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = AppDatabase.getsInstance(this);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mComponentType = intent.getIntExtra("component", AdminActivity.CPU_FRAGMENT);
        switch (mComponentType) {
            case AdminActivity.CPU_FRAGMENT:
                handleCpu();
                break;
            case AdminActivity.CARDS_FRAGMENT:
                handleCard();
                break;
            case AdminActivity.IOONBOARD_FRAGMENT:
                handleIOOnboard();
                break;
            case AdminActivity.MANUFACTURERS_FRAGMENT:
                handleManufacturer();
                break;
            case AdminActivity.PROTOCOLS_FRAGMENT:
                handleProtocol();
                break;
            default:
                setContentView(R.layout.add_component_cpu);
        }
    }

    private void handleCpu() {
        setContentView(R.layout.add_component_cpu);
        mActionBar.setTitle("Add CPU");

        ioSpinner_Cpu = findViewById(R.id.spinner_cpu_io);
        List<IOOnboard> ioonboards = mDb.ioOnboardDao().loadAllIOOnboards();
        List<String> ioOnboardsLabels = new ArrayList<>();
        if (ioonboards != null) {
            for (IOOnboard io : ioonboards) {
                ioOnboardsLabels.add(String.valueOf(io.getChannels()) + io.getName());
            }
        }

        if (ioOnboardsLabels.isEmpty()) {
            ioOnboardsLabels.add("No ioonboards in database");
        }

        ArrayAdapter<String> ioSpinnerArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_spinner_item, ioOnboardsLabels);
        ioSpinner_Cpu.setAdapter(ioSpinnerArrayAdapter);

        List<String> families = mDb.manufacturerDao().loadAllFamilies();
        if (families.isEmpty()) {
            families.add("No families in database");
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_item, families);
        familySpinner_Cpu = findViewById(R.id.spinner_cpu_manufacturer);
        familySpinner_Cpu.setAdapter(spinnerArrayAdapter);

        Button addProtocolButton = findViewById(R.id.button_cpu_add_protocol);
        Button addCardButton = findViewById(R.id.button_cpu_add_card);
        emptyProtocols = findViewById(R.id.emptytv_cpu_protocols);
        emptyCards = findViewById(R.id.emptytv_cpu_cards);
        protocolsRecyclerView = findViewById(R.id.recyclerview_cpu_protocols);
        protocolsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cardsRecyclerView = findViewById(R.id.recyclerview_cpu_cards);
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final LayoutInflater layoutInflater = getLayoutInflater();

        addProtocolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Protocol> protocols = mDb.protocolDao().loadAllProtocols();
                if(cpuId == -1){
                    addCpu(ioSpinner_Cpu, familySpinner_Cpu);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(AddComponentActivity.this);
                View dialog = layoutInflater.inflate(R.layout.dialog_spinner, null);
                final AlertDialog alertDialog = builder.setView(dialog)
                        .setTitle("Select Item: ")
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", null)
                        .create();

                final Spinner spinner = dialog.findViewById(R.id.dialog_spinner);
                List<String> protocolLabels = new ArrayList<>();
                for (Protocol protocol : protocols) {
                    protocolLabels.add(protocol.getName());
                }
                spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        protocolLabels));

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        buttonPositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int selectedItem = spinner.getSelectedItemPosition();
                                mDb.cpuProtocolDao().insert(new CPUProtocol(cpuId, mDb.protocolDao().loadAllProtocolIds().get(selectedItem)));
                                alertDialog.cancel();
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

        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Card> cards = mDb.cardDao().loadAllCards();
                if(cpuId == -1){
                    addCpu(ioSpinner_Cpu, familySpinner_Cpu);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(AddComponentActivity.this);
                View dialog = layoutInflater.inflate(R.layout.dialog_spinner, null);
                final AlertDialog alertDialog = builder.setView(dialog)
                        .setTitle("Select Item: ")
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", null)
                        .create();

                final Spinner spinner = dialog.findViewById(R.id.dialog_spinner);
                List<String> cardLabels = new ArrayList<>();
                for (Card card : cards) {
                    cardLabels.add(String.valueOf(card.getChannels())+card.getName());
                }
                spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        cardLabels));

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        buttonPositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int selectedItem = spinner.getSelectedItemPosition();
                                mDb.cpuCardDao().insert(new CPUCard(cpuId, mDb.cardDao().loadAllCardIds().get(selectedItem)));
                                alertDialog.cancel();
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

    private void handleCard() {
        setContentView(R.layout.add_component_card);
        mActionBar.setTitle("Add Card");

        List<String> families = mDb.manufacturerDao().loadAllFamilies();
        if (families.isEmpty()) {
            families.add("No families in the database");
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_spinner_item, families);
        familySpinner_Card = findViewById(R.id.spinner_card_family);
        familySpinner_Card.setAdapter(spinnerArrayAdapter);
        for (String string : families) {
            Log.e(CardsFragment.class.getName(), string);
        }
    }

    private void handleIOOnboard() {
        setContentView(R.layout.add_component_ioonboard);
        mActionBar.setTitle("Add I/O Onboard");
    }

    private void handleManufacturer() {
        setContentView(R.layout.add_component_manufacturer);
        mActionBar.setTitle("Add Manufacturer");
    }

    private void handleProtocol() {
        setContentView(R.layout.add_component_protocol);
        mActionBar.setTitle("Add Protocol");
    }

    private void addCpu(Spinner ioSpinner, Spinner familySpinner) {
        EditText nameField = findViewById(R.id.edittext_cpu_name);
        EditText memoryField = findViewById(R.id.edittext_cpu_memory);
        EditText supplyField = findViewById(R.id.edittext_cpu_supply);
        EditText priceField = findViewById(R.id.edittext_cpu_price);
        EditText codeField = findViewById(R.id.edittext_cpu_code);
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
        if (name.isEmpty() && memory == -1 && supply == -1 && price == -1 && code == -1) {
            finish();
            Toast.makeText(this, "No item was added to database", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty()) {
            nameField.setError("Must not be empty");
            notNull = false;
        }
        if (memory == -1) {
            memoryField.setError("Must not be empty");
            notNull = false;
        }
        if (supply == -1) {
            supplyField.setError("Must not be empty");
            notNull = false;
        }
        if (price == -1) {
            priceField.setError("Must not be empty");
            notNull = false;
        }
        if (code == -1) {
            codeField.setError("Must not be empty");
            notNull = false;
        }
        if (notNull) {
            CPU cpu = new CPU(name, memory, 0, code, supply, price, manufacturerId, ioId);
            cpuId = mDb.cpuDao().insertCpu(cpu);

            Toast.makeText(this, "CPU " + cpu.getName() + " inserted. Id = " + mDb.cpuDao().loadCpuByName(cpu.getName()), Toast.LENGTH_SHORT).show();
        }
        displayCPUProtocols();
        displayCPUCards();
    }

    private void addCard(Spinner familySpinner) {
        Spinner nameSpinner = findViewById(R.id.spinner_card_name);
        EditText channelsField = findViewById(R.id.edittext_card_channels);
        EditText typeField = findViewById(R.id.edittext_card_type);

        String name = nameSpinner.getSelectedItem().toString();
        int channels = Integer.valueOf(channelsField.getText().toString());
        String family = familySpinner.getSelectedItem().toString();
        String type = typeField.getText().toString();

        if (channelsField.getText().toString().equals("")) {
            if (type.equals("")) {
                finish();
                Toast.makeText(getApplicationContext(), "No item was added to database", Toast.LENGTH_SHORT).show();
                return;
            }
            channelsField.setError("Must not be empty");
        } else if (type.equals("")) {
            typeField.setError("Must not be empty");
        } else {
            int id = mDb.manufacturerDao().loadManufacturerForFamily(family);
            mDb.cardDao().insertCard(new Card(name, channels, id, type));
            finish();
        }
    }

    private void addIOOnboard() {
        Spinner nameSpinner = findViewById(R.id.spinner_ioonboard_name);
        EditText channelsField = findViewById(R.id.edittext_ioonboard_channels);

        if (channelsField.getText().toString().equals("")) {
            channelsField.setError("Must not be empty");
        } else {
            String name = nameSpinner.getSelectedItem().toString();
            int channels = Integer.valueOf(channelsField.getText().toString());

            mDb.ioOnboardDao().insertIOOnboard(new IOOnboard(name, channels));
            finish();
        }
    }

    private void addManufacturer() {
        EditText nameField = findViewById(R.id.edittext_manufacturer_name);
        EditText familyField = findViewById(R.id.edittext_manufacturer_family);

        String name = nameField.getText().toString();
        String family = familyField.getText().toString();

        if (name.equals("")) {
            if (family.equals("")) {
                finish();
                Toast.makeText(getApplicationContext(), "No item was added to database", Toast.LENGTH_SHORT).show();
                return;
            }
            nameField.setError("Must not be empty");
        } else if (family.equals("")) {
            familyField.setError("Must not be empty");
        } else {
            mDb.manufacturerDao().insertManufacturer(new Manufacturer(name, family));
            finish();
        }
    }

    private void addProtocol() {
        EditText nameField = findViewById(R.id.edittext_protocol_name);
        EditText interfaceField = findViewById(R.id.edittext_protocol_interf);
        RadioGroup typeGroup = findViewById(R.id.radiogroup_protocol_type);

        String name = nameField.getText().toString();
        String interf = interfaceField.getText().toString();
        int checkedRadioButtonId = typeGroup.getCheckedRadioButtonId();
        String type = ((RadioButton) findViewById(checkedRadioButtonId)).getText().toString();

        if (name.equals("")) {
            if (interf.equals("")) {
                finish();
                Toast.makeText(getApplicationContext(), "No item was added to database", Toast.LENGTH_SHORT).show();
                return;
            }
            nameField.setError("Must not be empty");
        } else if (interf.equals("")) {
            interfaceField.setError("Must not be empty");
        } else {
            mDb.protocolDao().insertProtocol(new Protocol(name, interf, type));
            finish();
        }
        Toast.makeText(this, "Protocol " + name + " inserted. Id = " + Integer.toString(mDb.protocolDao().loadProtocolByName(name).getId()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if(mComponentType == AdminActivity.CPU_FRAGMENT){
                    if(mDb.cpuDao().loadCpu(cpuId) != null){
                        mDb.cpuDao().deleteCpu(mDb.cpuDao().loadCpu(cpuId));
                    }
                }
                Intent upIntent = new Intent(this, AdminActivity.class);
                upIntent.putExtra("OpenFragment", mComponentType);
                NavUtils.navigateUpTo(this, upIntent);
                return true;
            case R.id.action_done:
                switch (mComponentType) {
                    case AdminActivity.CPU_FRAGMENT:
                        if(cpuId == -1){
                            addCpu(ioSpinner_Cpu, familySpinner_Cpu);
                        }
                        finish();
                        break;
                    case AdminActivity.CARDS_FRAGMENT:
                        addCard(familySpinner_Card);
                        break;
                    case AdminActivity.IOONBOARD_FRAGMENT:
                        addIOOnboard();
                        break;
                    case AdminActivity.MANUFACTURERS_FRAGMENT:
                        addManufacturer();
                        break;
                    case AdminActivity.PROTOCOLS_FRAGMENT:
                        addProtocol();
                        break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayCPUProtocols(){
        mDb.cpuProtocolDao().getProtocolsForCPU(cpuId).observe(this, new Observer<List<Protocol>>() {
            @Override
            public void onChanged(@android.support.annotation.Nullable List<Protocol> protocols) {
                final List<String> protocolLabels = new ArrayList<>();
                CardsProtocolsAdapter adapter = new CardsProtocolsAdapter(getApplicationContext(), cpuId, 10);
                if(protocols == null || protocols.isEmpty()){
                    protocolsRecyclerView.setVisibility(View.GONE);
                    emptyProtocols.setVisibility(View.VISIBLE);
                } else {
                    for(Protocol prot : protocols){
                        protocolLabels.add(prot.getName());
                    }
                    adapter.setItems(protocolLabels);
                    protocolsRecyclerView.setAdapter(adapter);
                    protocolsRecyclerView.setVisibility(View.VISIBLE);
                    emptyProtocols.setVisibility(View.GONE);
                }
            }
        });
    }

    public void displayCPUCards(){
        mDb.cpuCardDao().getCardsForCPUJoin(cpuId).observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@android.support.annotation.Nullable List<Card> cards) {
                final List<String> cardLabels = new ArrayList<>();
                CardsProtocolsAdapter adapter = new CardsProtocolsAdapter(getApplicationContext(), cpuId, 20);
                if(cards == null || cards.isEmpty()){
                    cardsRecyclerView.setVisibility(View.GONE);
                    emptyCards.setVisibility(View.VISIBLE);
                } else {
                    for(Card card : cards){
                        cardLabels.add(String.valueOf(card.getChannels())+card.getName());
                    }
                    adapter.setItems(cardLabels);
                    cardsRecyclerView.setAdapter(adapter);
                    cardsRecyclerView.setVisibility(View.VISIBLE);
                    emptyCards.setVisibility(View.GONE);
                }
            }
        });
    }
}
