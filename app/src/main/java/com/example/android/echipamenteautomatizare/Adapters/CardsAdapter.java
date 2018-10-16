package com.example.android.echipamenteautomatizare.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.echipamenteautomatizare.Objects.Card;
import com.example.android.echipamenteautomatizare.R;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {
    private List<Card> myCards;
    private Context mContext;

    public CardsAdapter(@NonNull Context context) {
        mContext = context;
    }

    public void setCards(List<Card> cards) {
        myCards = cards;
        notifyDataSetChanged();
    }

    public List<Card> getCards() {
        return myCards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Card card = myCards.get(position);
        holder.cardName.setText(card.getName());
        holder.cardChannels.setText(card.getChannels());
        holder.cardFamily.setText(card.getFamily());
        holder.cardType.setText(card.getType());
    }

    @Override
    public int getItemCount() {
        if (myCards == null) {
            return 0;
        }
        return myCards.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cardName;
        TextView cardChannels;
        TextView cardFamily;
        TextView cardType;

        MyViewHolder(View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.card_name);
            cardChannels = itemView.findViewById(R.id.card_channels);
            cardFamily = itemView.findViewById(R.id.card_family);
            cardType = itemView.findViewById(R.id.card_type);
        }
    }
}
