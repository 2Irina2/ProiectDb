package com.example.android.echipamenteautomatizare.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.echipamenteautomatizare.Objects.Protocol;
import com.example.android.echipamenteautomatizare.R;

import java.util.List;

public class ProtocolsAdapter extends RecyclerView.Adapter<ProtocolsAdapter.MyViewHolder> {

    private List<Protocol> myProtocols;
    private Context mContext;

    public ProtocolsAdapter(@NonNull Context context) {
        mContext = context;
    }

    public void setProtocols(List<Protocol> protocols) {
        myProtocols = protocols;
        notifyDataSetChanged();
    }

    public List<Protocol> getProtocols() {
        return myProtocols;
    }

    @NonNull
    @Override
    public ProtocolsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_protocol, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProtocolsAdapter.MyViewHolder holder, int position) {
        Protocol protocol = myProtocols.get(position);
        holder.protocolName.setText(protocol.getName());
        holder.protocolInterface.setText(protocol.getInterf());
        holder.protocolType.setText(protocol.getType());
    }

    @Override
    public int getItemCount() {
        if (myProtocols == null) {
            return 0;
        }
        return myProtocols.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView protocolName;
        TextView protocolInterface;
        TextView protocolType;

        MyViewHolder(View itemView) {
            super(itemView);
            protocolName = itemView.findViewById(R.id.protocol_name);
            protocolInterface = itemView.findViewById(R.id.protocol_interface);
            protocolType = itemView.findViewById(R.id.protocol_type);
        }
    }
}
