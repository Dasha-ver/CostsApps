package com.example.costsapp;

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

class MyAdapterForLineView extends RecyclerView.Adapter<MyAdapterForLineView.MyViewHolder> {

    private List<Costs> costsList;
    private GetMyItem mGetMyItem;
    String checkedCategory;

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageButton avatar;
        TextView name;

        MyViewHolder(final View view) {
            super(view);
            avatar = view.findViewById(R.id.avatar_liner);
            name = view.findViewById(R.id.name_liner);

            avatar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    checkedCategory = name.getText().toString();
                    mGetMyItem.getMyItem(view, checkedCategory);// категории через интерфейс
                }
            });
        }
    }

    public MyAdapterForLineView(List<Costs> costsList, GetMyItem getMyItem) {
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

    }

    @Override
    public int getItemCount() {
        return costsList.size();
    }
}
