package com.mp.rena.craftersnote;

import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RequiredMaterialAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> list;
    private Context context;
    TextView title;
    Button addToday, addEveryDay;
    ImageView iconRM;

    public RequiredMaterialAdapter(Context context, ArrayList<Item> list) {
        super(context, 0, list);
        this.list = list;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter_layout, parent, false);
        }

        final Item currentItem = getItem(position);
        title = listItemView.findViewById(R.id.rowTextViewRM);
        addToday = listItemView.findViewById(R.id.addTodayBtnRM);
        addEveryDay = listItemView.findViewById(R.id.addEverydayBtnRM);
        title.setText(currentItem.name+ " (" + currentItem.quantity + ")");
        iconRM = listItemView.findViewById(R.id.itemIconRM);

        Glide.with(context)
                .load(R.drawable.item_020001)
                .apply(RequestOptions.placeholderOf(new ColorDrawable(Color.BLACK)))
                .apply(RequestOptions.overrideOf(150, 150))
                .apply(RequestOptions.circleCropTransform())
                .into(iconRM);

        addToday.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
               MainActivity.db.insertToday(currentItem);
            }
        });

        addEveryDay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.db.insertToday(currentItem);
                MainActivity.db.insertEveryday(currentItem);
            }
        });

        return listItemView;
    }
}
