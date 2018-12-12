package com.example.android.echipamenteautomatizare.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.echipamenteautomatizare.AppDatabase;
import com.example.android.echipamenteautomatizare.R;

import java.util.List;

public class CardsProtocolsAdapter extends RecyclerView.Adapter<CardsProtocolsAdapter.MyViewHolder> {
    public static final int TYPE_PROTOCOL = 10;
    public static final int TYPE_CARD = 20;

    List<String> mItems;
    long mCpuId;
    Context mContext;
    int mType;

    public CardsProtocolsAdapter(@NonNull Context context, long cpuId, int type) {
        mContext = context;
        mCpuId = cpuId;
        mType = type;
    }

    public void setItems(List<String> items){
        mItems = items;
        notifyDataSetChanged();
    }

    public List<String> getItems(){
        return mItems;
    }

    @NonNull
    @Override
    public CardsProtocolsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardsProtocolsAdapter.MyViewHolder holder, int position) {
        holder.itemName.setText(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        if(mItems == null){
            return 0;
        }
        return mItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemName;
        ImageView clear;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.list_item_item_name);
            clear = itemView.findViewById(R.id.clear);
            clear.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AppDatabase db = AppDatabase.getsInstance(mContext);
            if(mType == TYPE_PROTOCOL){
                int protocolId = db.protocolDao().loadProtocolByName(itemName.getText().toString()).getId();
                db.cpuProtocolDao().deleteCPUProtocol(mCpuId, protocolId);
            } else {
                String entry = itemName.getText().toString();
                int channels = Integer.valueOf(entry.substring(0, entry.length()-2));
                String name = entry.substring(entry.length()-2, entry.length());
                int cardId = db.cardDao().loadCard(channels, name).getId();
                db.cpuCardDao().deleteCPUCard(mCpuId, cardId);
            }
        }
    }
}
