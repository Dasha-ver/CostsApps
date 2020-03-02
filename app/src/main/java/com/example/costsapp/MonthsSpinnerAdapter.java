package com.example.costsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MonthsSpinnerAdapter extends ArrayAdapter<FoursPageSpinnerInfo> {
    private Activity context;
    ArrayList<FoursPageSpinnerInfo> data = null;

    public MonthsSpinnerAdapter(Activity context, int resource, ArrayList<FoursPageSpinnerInfo> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    //установка заголовка на спиннер
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, R.layout.dropdownheader_month, null);
        }
        return convertView;
    }

    //этот код выполняется, когда вы нажимаете на спиннер
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.month_spinner, parent, false);
        }
        FoursPageSpinnerInfo item = data.get(position);
        // парсим данные с каждого объекта
        if (item != null) {
            TextView myName = (TextView) row.findViewById(R.id.name_month_spin);
            if (myName != null)
                myName.setText(item.getName());
        }
        return row;
    }

}
