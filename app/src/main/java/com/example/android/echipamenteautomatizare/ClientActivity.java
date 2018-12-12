package com.example.android.echipamenteautomatizare;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.Objects.IOOnboard;
import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.Objects.Protocol;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    EditText specsMemory;
    EditText specsSupply;
    EditText specsMaxPrice;
    Spinner specsManufacturer;
    Spinner specsIOOnboard;
    Button showAll;
    Button searchMan;
    Button searchIO;
    Button searchProtocol;
    Button searchCard;

    AppDatabase mDb;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("CPU Specs");

        mDb = AppDatabase.getsInstance(this);
        layoutInflater = getLayoutInflater();
        specsMemory = findViewById(R.id.specs_memory);
        specsSupply = findViewById(R.id.specs_supply);
        specsMaxPrice = findViewById(R.id.specs_price);
        showAll = findViewById(R.id.specs_show_all);
        searchMan = findViewById(R.id.specs_search_manufacturer);
        searchIO = findViewById(R.id.specs_search_ioonboard);
        searchProtocol = findViewById(R.id.specs_search_protocol);
        searchCard = findViewById(R.id.specs_search_card);

        final StringBuilder stringBuilder = new StringBuilder();
        final StringBuilder countStringBuilder = new StringBuilder();
        final StringBuilder averageStringBuilder = new StringBuilder();

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringBuilder.setLength(0);
                stringBuilder.append("SELECT * FROM cpus ");
                countStringBuilder.setLength(0);
                countStringBuilder.append("SELECT count(*) FROM cpus ");
                averageStringBuilder.setLength(0);
                averageStringBuilder.append("SELECT avg(price) FROM cpus ");
                appendBasicProperties(stringBuilder);
                appendBasicProperties(countStringBuilder);
                appendBasicProperties(averageStringBuilder);
                startActivity(new Intent(getApplicationContext(), OfferActivity.class)
                        .putExtra("query", stringBuilder.toString())
                        .putExtra("countQuery", countStringBuilder.toString())
                        .putExtra("averageQuery", averageStringBuilder.toString()));
                //Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
            }
        });

        searchMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringBuilder.setLength(0);
                stringBuilder.append("SELECT * FROM cpus ");
                countStringBuilder.setLength(0);
                countStringBuilder.append("SELECT count(*) FROM cpus ");
                averageStringBuilder.setLength(0);
                averageStringBuilder.append("SELECT avg(price) FROM cpus ");
                appendBasicProperties(stringBuilder);
                appendBasicProperties(countStringBuilder);
                appendBasicProperties(averageStringBuilder);

                List<Manufacturer> manufacturers = mDb.manufacturerDao().loadAllManufacturers();

                AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);
                View dialog = layoutInflater.inflate(R.layout.dialog_spinner, null);
                final AlertDialog alertDialog = builder.setView(dialog)
                        .setTitle("Select Item: ")
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", null)
                        .create();

                final Spinner spinner = dialog.findViewById(R.id.dialog_spinner);
                List<String> manufacturerLabels = new ArrayList<>();
                for (Manufacturer manufacturer : manufacturers) {
                    manufacturerLabels.add(manufacturer.getName());
                }
                spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        manufacturerLabels));

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        buttonPositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String selectedMan = spinner.getSelectedItem().toString();
                                if (stringBuilder.toString().equals("SELECT * FROM cpus ")) {
                                    stringBuilder.append("WHERE ");
                                } else {
                                    stringBuilder.append(" AND ");
                                }
                                if (countStringBuilder.toString().equals("SELECT count(*) FROM cpus ")) {
                                    countStringBuilder.append("WHERE ");
                                } else {
                                    countStringBuilder.append(" AND ");
                                }
                                if (averageStringBuilder.toString().equals("SELECT avg(price) FROM cpus ")) {
                                    averageStringBuilder.append("WHERE ");
                                } else {
                                    averageStringBuilder.append(" AND ");
                                }
                                String manufacturerSubQuery = "manufacturerId IN (SELECT id FROM manufacturers WHERE name='" + selectedMan + "')";
                                stringBuilder.append(manufacturerSubQuery);
                                countStringBuilder.append(manufacturerSubQuery);
                                averageStringBuilder.append(manufacturerSubQuery);
                                startActivity(new Intent(getApplicationContext(), OfferActivity.class)
                                        .putExtra("query", stringBuilder.toString())
                                        .putExtra("countQuery", countStringBuilder.toString())
                                        .putExtra("averageQuery", averageStringBuilder.toString()));
                                alertDialog.cancel();
                                //Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                        Button buttonNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        buttonNegative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.cancel();
                                //Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });

        searchIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringBuilder.setLength(0);
                stringBuilder.append("SELECT * FROM cpus ");
                countStringBuilder.setLength(0);
                countStringBuilder.append("SELECT count(*) FROM cpus ");
                averageStringBuilder.setLength(0);
                averageStringBuilder.append("SELECT avg(price) FROM cpus ");
                appendBasicProperties(stringBuilder);
                appendBasicProperties(countStringBuilder);
                appendBasicProperties(averageStringBuilder);

                List<IOOnboard> ioOnboards = mDb.ioOnboardDao().loadAllIOOnboards();

                AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);
                View dialog = layoutInflater.inflate(R.layout.dialog_spinner, null);
                final AlertDialog alertDialog = builder.setView(dialog)
                        .setTitle("Select Item: ")
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", null)
                        .create();

                final Spinner spinner = dialog.findViewById(R.id.dialog_spinner);
                List<String> ioonboardLabels = new ArrayList<>();
                for (IOOnboard ioOnboard : ioOnboards) {
                    ioonboardLabels.add(ioOnboard.getChannels() + ioOnboard.getName());
                }
                spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        ioonboardLabels));

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        buttonPositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String selectedIo = spinner.getSelectedItem().toString();
                                int channels = Integer.valueOf(selectedIo.substring(0, selectedIo.length() - 2));
                                String name = selectedIo.substring(selectedIo.length() - 2, selectedIo.length());
                                if (stringBuilder.toString().equals("SELECT * FROM cpus ")) {
                                    stringBuilder.append("WHERE ");
                                } else {
                                    stringBuilder.append(" AND ");
                                }
                                if (countStringBuilder.toString().equals("SELECT count(*) FROM cpus ")) {
                                    countStringBuilder.append("WHERE ");
                                } else {
                                    countStringBuilder.append(" AND ");
                                }
                                if (averageStringBuilder.toString().equals("SELECT count(*) FROM cpus ")) {
                                    averageStringBuilder.append("WHERE ");
                                } else {
                                    averageStringBuilder.append(" AND ");
                                }
                                String ioOnboardSubQuery = "ioonboardId IN (SELECT id FROM ioonboards WHERE name='" + name + "' AND channels='" + channels + "')";
                                stringBuilder.append(ioOnboardSubQuery);
                                countStringBuilder.append(ioOnboardSubQuery);
                                averageStringBuilder.append(ioOnboardSubQuery);
                                startActivity(new Intent(getApplicationContext(), OfferActivity.class)
                                        .putExtra("query", stringBuilder.toString())
                                        .putExtra("countQuery", countStringBuilder.toString())
                                        .putExtra("averageQuery", averageStringBuilder.toString()));
                                alertDialog.cancel();
                                //Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                        Button buttonNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        buttonNegative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.cancel();
                                //Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });

        searchProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Protocol> protocols = mDb.protocolDao().loadAllProtocols();

                AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);
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
                                String selectedProtocol = spinner.getSelectedItem().toString();
                                Protocol p = mDb.protocolDao().loadProtocolByName(selectedProtocol);
                                startActivity(new Intent(getApplicationContext(), OfferActivity.class)
                                        .putExtra("protocolId", p.getId()));
                                alertDialog.cancel();
                                //Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                        Button buttonNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        buttonNegative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.cancel();
                                //Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });

        searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Card> cards = mDb.cardDao().loadAllCards();

                AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);
                View dialog = layoutInflater.inflate(R.layout.dialog_spinner, null);
                final AlertDialog alertDialog = builder.setView(dialog)
                        .setTitle("Select Item: ")
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", null)
                        .create();

                final Spinner spinner = dialog.findViewById(R.id.dialog_spinner);
                List<String> cardLabels = new ArrayList<>();
                for (Card card : cards) {
                    cardLabels.add(card.getChannels() + card.getName());
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
                                String selectedCard = spinner.getSelectedItem().toString();
                                int channels = Integer.valueOf(selectedCard.substring(0, selectedCard.length()-2));
                                String name = selectedCard.substring(selectedCard.length()-2, selectedCard.length());
                                startActivity(new Intent(getApplicationContext(), OfferActivity.class)
                                        .putExtra("cardId", mDb.cardDao().loadCard(channels, name).getId()));
                                alertDialog.cancel();
                                //Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                        Button buttonNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        buttonNegative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.cancel();
                                //Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });

    }

    private void appendBasicProperties(StringBuilder stringBuilder) {
        String memory = specsMemory.getText().toString();
        String supply = specsSupply.getText().toString();
        String maxPrice = specsMaxPrice.getText().toString();

        if (memory.isEmpty() && supply.isEmpty() && maxPrice.isEmpty()) {
        } else {
            stringBuilder.append("WHERE ");
            if (!memory.isEmpty()) {
                stringBuilder.append("memory=").append(memory);
                if (!supply.isEmpty()) {
                    stringBuilder.append(" AND ");
                    stringBuilder.append("supply=").append(supply);
                }
                if (!maxPrice.isEmpty()) {
                    stringBuilder.append(" AND ");
                    stringBuilder.append("price<").append(maxPrice);
                }
            } else if (!supply.isEmpty()) {
                stringBuilder.append("supply=").append(supply);
                if (!maxPrice.isEmpty()) {
                    stringBuilder.append(" AND ");
                    stringBuilder.append("price<").append(maxPrice);
                }
            } else {
                stringBuilder.append("price<").append(maxPrice);
            }
        }
    }
}
