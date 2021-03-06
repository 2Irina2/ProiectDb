package com.example.android.echipamenteautomatizare.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.echipamenteautomatizare.AppDatabase;
import com.example.android.echipamenteautomatizare.Objects.CPU;
import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.Objects.Protocol;
import com.example.android.echipamenteautomatizare.R;

import java.util.ArrayList;
import java.util.List;

public class CPUsAdapter extends RecyclerView.Adapter<CPUsAdapter.MyViewHolder> {
    private List<CPU> myCpus;
    private Context mContext;

    public CPUsAdapter(@NonNull Context context) {
        mContext = context;
    }

    public void setCPUs(List<CPU> cpus) {
        myCpus = cpus;
        notifyDataSetChanged();
    }

    public List<CPU> getCpus() {
        return myCpus;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_cpu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CPU cpu = myCpus.get(position);
        holder.cpuName.setText(cpu.getName());
        holder.cpuMemory.setText(String.valueOf(cpu.getMemory()));
        int channels = AppDatabase.getsInstance(mContext)
                .ioOnboardDao()
                .loadChannelsForIOOnboard(cpu.getIoonboardId());
        String name = AppDatabase.getsInstance(mContext)
                .ioOnboardDao()
                .loadNameForIOOnboard(cpu.getIoonboardId());
        holder.cpuIO.setText(String.valueOf(channels) + name);
        holder.cpuSupply.setText(String.valueOf(cpu.getSupply()));
        String manufacturerFamily = AppDatabase.getsInstance(mContext)
                .manufacturerDao()
                .loadFamilyForManufacturer(cpu.getManufacturerId());
        holder.cpuManufacturer.setText(manufacturerFamily);
        holder.cpuCode.setText(String.valueOf(cpu.getCode()));
        holder.cpuPrice.setText(String.valueOf(cpu.getPrice()));

        AppDatabase db = AppDatabase.getsInstance(mContext);
        List<Protocol> protocols = db.cpuProtocolDao().getProtocolsForCPUSelect(cpu.getId());
        List<Card> cards = db.cpuCardDao().getCardsForCPUSelect(cpu.getId());

        StringBuilder builder = new StringBuilder();
        for(Protocol protocol:protocols){
            builder.append(protocol.getName()).append(" ");
        }
        holder.cpuProtocols.setText(builder.toString());
        builder.setLength(0);
        for(Card card : cards){
            builder.append(card.getChannels()).append(card.getName()).append(" ");
        }
        holder.cpuCards.setText(builder.toString());
    }

    @Override
    public int getItemCount() {
        if (myCpus == null) {
            return 0;
        }
        return myCpus.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cpuName;
        TextView cpuMemory;
        TextView cpuIO;
        TextView cpuSupply;
        TextView cpuManufacturer;
        TextView cpuPrice;
        TextView cpuCode;
        TextView cpuProtocols;
        TextView cpuCards;

        MyViewHolder(View itemView) {
            super(itemView);
            cpuName = itemView.findViewById(R.id.cpu_name);
            cpuMemory = itemView.findViewById(R.id.cpu_memory);
            cpuIO = itemView.findViewById(R.id.cpu_io);
            cpuSupply = itemView.findViewById(R.id.cpu_supply);
            cpuManufacturer = itemView.findViewById(R.id.cpu_manufacturer);
            cpuPrice = itemView.findViewById(R.id.cpu_price);
            cpuCode = itemView.findViewById(R.id.cpu_code);
            cpuProtocols = itemView.findViewById(R.id.cpu_protocols);
            cpuCards = itemView.findViewById(R.id.cpu_cards);
        }
    }
}

