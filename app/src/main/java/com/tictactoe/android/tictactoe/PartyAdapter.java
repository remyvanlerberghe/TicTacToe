package com.tictactoe.android.tictactoe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tictactoe.android.tictactoe.Models.PartyShort;

import java.util.ArrayList;

public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.DataObjectHolder> {
    private ArrayList<PartyShort> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titre, description;
        Button join;

        public DataObjectHolder(View itemView) {
            super(itemView);
            titre = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);

            join = (Button) itemView.findViewById(R.id.join);
            join.setOnClickListener(this);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public PartyAdapter(ArrayList<PartyShort> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_party, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
//        holder.titre.setText(mDataset.get(position).getId());
        holder.titre.setText(" ");
        holder.description.setText(mDataset.get(position).getUser());
    }

    public void addItem(PartyShort   dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}