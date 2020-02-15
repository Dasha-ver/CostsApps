package com.example.costsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    String checkedCategory;
    private List<Costs> costsList;
    private GetMyItem mGetMyItem;

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageButton avatar;
        TextView name;

        MyViewHolder(final View view) {
            super(view);
            avatar = view.findViewById(R.id.avatar);
            name = view.findViewById(R.id.name);


            avatar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    checkedCategory = name.getText().toString();
                    mGetMyItem.getMyItem(view, checkedCategory);//передача категории через интерфейс
                }
            });
        }
    }

    public MyAdapter(List<Costs> costsList, GetMyItem getMyItem) {
        this.costsList = costsList;
        mGetMyItem = getMyItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_item_for_grid, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Costs c = costsList.get(position);
        holder.name.setText(c.getName());
        holder.avatar.setImageResource(c.getAvatarId());
    }

    @Override
    public int getItemCount() {
        return costsList.size();
    }
}