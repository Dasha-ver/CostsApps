package com.example.costsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    String amount;
    String checkedCategory;
    String hint;
    private List<Costs> costsList;
    private GetMyItem mGetMyItem;

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name;
        EditText expenses;
        Button check;

        MyViewHolder(final View view) {
            super(view);
            avatar = view.findViewById(R.id.avatar);
            name = view.findViewById(R.id.name);
            expenses = view.findViewById(R.id.man_grid);
            check = view.findViewById(R.id.grid_button);

            check.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    amount = expenses.getText().toString();
                    checkedCategory = name.getText().toString();
                    mGetMyItem.getMyItem(view, amount, checkedCategory);//передача суммы и категории через интерфейс
                    expenses.setText("");
                }
            });
        }
    }

    public MyAdapter(List<Costs> costsList, String hint, GetMyItem getMyItem) {
        this.costsList = costsList;
        this.hint = hint;
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
        holder.expenses.setHint(hint);
    }

    @Override
    public int getItemCount() {
        return costsList.size();
    }
}