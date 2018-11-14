package com.example.android.echipamenteautomatizare.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.echipamenteautomatizare.Objects.IOOnboard;
import com.example.android.echipamenteautomatizare.R;

import java.util.List;

public class IOOnboardAdapter extends RecyclerView.Adapter<IOOnboardAdapter.MyViewHolder> {
    private List<IOOnboard> myIOOnboards;
    private Context mContext;

    public IOOnboardAdapter(Context context) {
        mContext = context;
    }

    public void setIOOnboards(List<IOOnboard> ioOnboards) {
        myIOOnboards = ioOnboards;
        notifyDataSetChanged();
    }

    public List<IOOnboard> getIOOnboards() {
        return myIOOnboards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IOOnboardAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_ioonboard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        IOOnboard ioOnboard = myIOOnboards.get(position);
        holder.ioOnboardName.setText(ioOnboard.getName());
        holder.ioOnboardChannels.setText(String.valueOf(ioOnboard.getChannels()));
    }

    @Override
    public int getItemCount() {
        if (myIOOnboards == null) {
            return 0;
        }
        return myIOOnboards.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ioOnboardName;
        TextView ioOnboardChannels;

        public MyViewHolder(View itemView) {
            super(itemView);
            ioOnboardName = itemView.findViewById(R.id.ioonboard_name);
            ioOnboardChannels = itemView.findViewById(R.id.ioonboard_channels);
        }
    }
}
