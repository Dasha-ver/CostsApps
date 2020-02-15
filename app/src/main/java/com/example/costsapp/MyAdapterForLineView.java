package com.example.costsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

class MyAdapterForLineView extends RecyclerView.Adapter<MyAdapterForLineView.MyViewHolder> {

    private List<Costs> costsList;
    private GetMyItem mGetMyItem;
    String amount;
    String checkedCategory;
    String hint;

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name;
        EditText expenses;
        Button check;


        MyViewHolder(final View view) {
            super(view);
            avatar = view.findViewById(R.id.avatar_liner);
            name = view.findViewById(R.id.name_liner);
            expenses = view.findViewById(R.id.man_liner);
            check = view.findViewById(R.id.liner_button);


            check.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    amount = expenses.getText().toString();
                    checkedCategory = name.getText().toString();
                    mGetMyItem.getMyItem(view, amount, checkedCategory);///передача суммы и категории через интерфейс
                    expenses.setText("");
                }
            });
        }
    }

    public MyAdapterForLineView(List<Costs> costsList, String hint, GetMyItem getMyItem) {
        this.hint = hint;
        this.costsList = costsList;
        mGetMyItem = getMyItem;
    }

    @Override
    public MyAdapterForLineView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_item_for_liner, parent, false);
        return new MyAdapterForLineView.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyAdapterForLineView.MyViewHolder holder, int position) {
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
