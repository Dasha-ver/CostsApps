package com.example.costsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

//адаптер для спиннера страницы для расчёта расходов

public class SpinnerAdapter extends ArrayAdapter<ThirdPageSpinnerInfo> {
    private Activity context;
    ArrayList<ThirdPageSpinnerInfo> data = null;

    public SpinnerAdapter(Activity context, int resource, ArrayList<ThirdPageSpinnerInfo> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    //установка заголовка на спиннер
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, R.layout.dropdown_header, null);
        }
        return convertView;
    }

    //этот код выполняется, когда вы нажимаете на спиннер
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_layout, parent, false);
        }
        ThirdPageSpinnerInfo item = data.get(position);
        // парсим данные с каждого объекта
        if (item != null) {
            ImageView myImage = (ImageView) row.findViewById(R.id.imageIcon);
            TextView myCategory = (TextView) row.findViewById(R.id.name_custom_spin);
            if (myImage != null) {
                myImage.setBackground(context.getResources().getDrawable(item.getCategoryPicture(), context.getTheme()));
            }
            if (myCategory != null)
                myCategory.setText(item.getCategoryName());
        }
        return row;
    }

}