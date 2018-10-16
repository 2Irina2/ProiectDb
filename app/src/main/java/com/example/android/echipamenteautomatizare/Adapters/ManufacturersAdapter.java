package com.example.android.echipamenteautomatizare.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.echipamenteautomatizare.Objects.Manufacturer;
import com.example.android.echipamenteautomatizare.R;

import java.util.List;

public class ManufacturersAdapter extends RecyclerView.Adapter<ManufacturersAdapter.MyViewHolder>{

    private List<Manufacturer> myManufacturers;
    private Context mContext;

    public ManufacturersAdapter(@NonNull Context context) {
        mContext = context;
    }

    public void setManufacturers(List<Manufacturer> manufacturers){
        myManufacturers = manufacturers;
        notifyDataSetChanged();
    }

    public List<Manufacturer> getManufacturers() {
        return myManufacturers;
    }

    @NonNull
    @Override
    public ManufacturersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_manufacturer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManufacturersAdapter.MyViewHolder holder, int position) {
        holder.nameManufacturer.setText(myManufacturers.get(position).getName());
        holder.familyManufacturer.setText(myManufacturers.get(position).getFamily());
    }

    @Override
    public int getItemCount() {
        if(myManufacturers == null)
            return 0;
        return myManufacturers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameManufacturer;
        TextView familyManufacturer;

        MyViewHolder(View itemView) {
            super(itemView);
            nameManufacturer = itemView.findViewById(R.id.manufacturer_name);
            familyManufacturer = itemView.findViewById(R.id.manufacturer_family);
        }
    }
}
