package com.example.android.echipamenteautomatizare;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.echipamenteautomatizare.Fragments.CardsFragment;
import com.example.android.echipamenteautomatizare.Objects.CPU;
import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.Objects.IOOnboard;
import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.Objects.Protocol;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AddComponentActivity extends AppCompatActivity {

    ActionBar mActionBar;
    AppDatabase mDb;
    int mComponentType;

    Spinner familySpinner_Cpu;
    Spinner familySpinner_Card;
    Spinner ioSpinner_Cpu;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = AppDatabase.getsInstance(this);
        
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mComponentType = intent.getIntExtra("component", AdminActivity.CPU_FRAGMENT);
        switch (mComponentType){
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
                setContentView(R.layout.dialog_cpu);
        }
    }
    
    private void handleCpu(){
        setContentView(R.layout.dialog_cpu);
        mActionBar.setTitle("Add CPU");

        ioSpinner_Cpu = findViewById(R.id.spinner_cpu_io);
        mDb.ioOnboardDao().loadAllIOOnboards().observe(this, new Observer<List<IOOnboard>>() {
            @Override
            public void onChanged(@Nullable List<IOOnboard> ioonboards) {
                List<String> ioOnboardsLabels = new ArrayList<>();
                if(ioonboards != null){
                    for(IOOnboard io:ioonboards){
                        ioOnboardsLabels.add(String.valueOf(io.getChannels()) + io.getName());
                    }
                }

                if(ioOnboardsLabels.isEmpty()){
                    ioOnboardsLabels.add("No ioonboards in database");
                }

                ArrayAdapter<String> ioSpinnerArrayAdapter = new ArrayAdapter<String>(
                        getApplicationContext(), android.R.layout.simple_spinner_item, ioOnboardsLabels);
                ioSpinner_Cpu.setAdapter(ioSpinnerArrayAdapter);
            }
        });

        List<String> families = mDb.manufacturerDao().loadAllFamilies();
        if (families.isEmpty()){
            families.add("No families in database");
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_item, families);
        familySpinner_Cpu = findViewById(R.id.spinner_cpu_manufacturer);
        familySpinner_Cpu.setAdapter(spinnerArrayAdapter);
    }

    private void handleCard(){
        setContentView(R.layout.dialog_card);
        mActionBar.setTitle("Add Card");

        List<String> families = mDb.manufacturerDao().loadAllFamilies();
        if (families.isEmpty()){
            families.add("No families in the database");
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_spinner_item, families);
        familySpinner_Card = findViewById(R.id.spinner_card_family);
        familySpinner_Card.setAdapter(spinnerArrayAdapter);
        for(String string : families){
            Log.e(CardsFragment.class.getName(), string);
        }
    }

    private void handleIOOnboard(){
        setContentView(R.layout.dialog_ioonboard);
        mActionBar.setTitle("Add I/O Onboard");
    }
    
    private void handleManufacturer(){
        setContentView(R.layout.dialog_manufacturer);
        mActionBar.setTitle("Add Manufacturer");
    }
    
    private void handleProtocol(){
        setContentView(R.layout.dialog_protocol);
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
        if(name.isEmpty() && memory == -1 && supply == -1 && price == -1 && code == -1){
            finish();
            Toast.makeText(this, "No item was added to database", Toast.LENGTH_SHORT).show();
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
            finish();
        }
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

    private void addIOOnboard(){
        Spinner nameSpinner = findViewById(R.id.spinner_ioonboard_name);
        EditText channelsField = findViewById(R.id.edittext_ioonboard_channels);

        if(channelsField.getText().toString().equals("")){
            channelsField.setError("Must not be empty");
        } else {
            String name = nameSpinner.getSelectedItem().toString();
            int channels = Integer.valueOf(channelsField.getText().toString());

            mDb.ioOnboardDao().insertIOOnboard(new IOOnboard(name, channels));
            finish();        
        }
    }

    private void addManufacturer(){
        EditText nameField = findViewById(R.id.edittext_manufacturer_name);
        EditText familyField = findViewById(R.id.edittext_manufacturer_family);

        String name = nameField.getText().toString();
        String family = familyField.getText().toString();

        if(name.equals("")){
            if(family.equals("")){
                finish();
                Toast.makeText(getApplicationContext(), "No item was added to database", Toast.LENGTH_SHORT).show();
                return;
            }
            nameField.setError("Must not be empty");
        } else if(family.equals("")){
            familyField.setError("Must not be empty");
        } else {
            mDb.manufacturerDao().insertManufacturer(new Manufacturer(name, family));
            finish();
        }
    }

    private void addProtocol(){
        EditText nameField = findViewById(R.id.edittext_protocol_name);
        EditText interfaceField = findViewById(R.id.edittext_protocol_interf);
        RadioGroup typeGroup = findViewById(R.id.radiogroup_protocol_type);

        String name = nameField.getText().toString();
        String interf = interfaceField.getText().toString();
        int checkedRadioButtonId = typeGroup.getCheckedRadioButtonId();
        String type = ((RadioButton) findViewById(checkedRadioButtonId)).getText().toString();

        if(name.equals("")){
            if(interf.equals("")){
                finish();
                Toast.makeText(getApplicationContext(), "No item was added to database", Toast.LENGTH_SHORT).show();
                return;
            }
            nameField.setError("Must not be empty");
        } else if(interf.equals("")){
            interfaceField.setError("Must not be empty");
        } else {
            mDb.protocolDao().insertProtocol(new Protocol(name, interf, type));
            finish();
        }
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
                Intent upIntent = new Intent(this, AdminActivity.class);
                upIntent.putExtra("OpenFragment", mComponentType);
                NavUtils.navigateUpTo(this, upIntent);
                return true;
            case R.id.action_done:
                switch (mComponentType){
                    case AdminActivity.CPU_FRAGMENT:
                        addCpu(ioSpinner_Cpu, familySpinner_Cpu);
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
}
