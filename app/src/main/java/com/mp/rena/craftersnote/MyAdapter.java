package com.mp.rena.craftersnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Item> list;
    private Context context;
    static Boolean isSearch = false;

    public MyAdapter(Context context, ArrayList<Item> list) {
        this.list = list;
        this.context = context;
    }

    public boolean isTodayFragment() {
        FragmentManager m = ((AppCompatActivity) context).getSupportFragmentManager();
        Fragment fragment = m.findFragmentByTag("0");
        if (fragment != null && fragment.isVisible()) {
            return true;
        }
        return false;
    }

    public boolean isEveryFragment() {
        FragmentManager m = ((AppCompatActivity) context).getSupportFragmentManager();
        Fragment fragment = m.findFragmentByTag("1");
        if (fragment != null && fragment.isVisible()) {
            return true;
        }
        return false;
    }


    public boolean isSearchFragment() {
        FragmentManager m = ((AppCompatActivity) context).getSupportFragmentManager();
        Fragment fragment = m.findFragmentByTag("4");
        if (fragment != null && fragment.isVisible()) {
            return true;
        }
        return false;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RelativeLayout layout;
        if (isSearchFragment()) {
            isSearch = true;
            layout = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_search_view, parent, false);
        } else {
            isSearch = false;
            layout = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view, parent, false);
        }

        MyViewHolder viewHolder = new MyViewHolder(layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textView.setText(list.get(position).name);
        Glide.with(context)
                .load(R.drawable.item_020001)
                .apply(RequestOptions.placeholderOf(new ColorDrawable(Color.BLACK)))
                .apply(RequestOptions.overrideOf(150, 150))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.itemIcon);

        if (isSearch){
            holder.todayBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item item = list.get(position);
                    if (!TodaysTask.list.contains(item)){
                        TodaysTask.list.add(item);
                        TodaysTask.adapter.notifyDataSetChanged();
                    }
                }
            });
            holder.everydayBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item item = list.get(position);
                    if (!TodaysTask.list.contains(item)){
                        TodaysTask.list.add(item);
                        TodaysTask.adapter.notifyDataSetChanged();
                    }
                    if (!ManageTask.list.contains(item)){
                        ManageTask.list.add(item);
                        ManageTask.adapter.notifyDataSetChanged();
                    }
                }
            });
            holder.detailBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder;
                    final AlertDialog alertDialog;
                    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.dialog_custom_layout, null);
                    builder = new AlertDialog.Builder(context);
                    builder.setView(layout);
                    alertDialog = builder.create();
                    ArrayList<String> materialList = new ArrayList<>();
                    materialList.add("1");
                    ListView listView = layout.findViewById(R.id.requiredMaterialDetail);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(layout.getContext(),android.R.layout.simple_list_item_1, materialList);
                    listView.setAdapter(arrayAdapter);
                    ImageButton closeBtn = layout.findViewById(R.id.closeDialog);
                    closeBtn.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });

        } else {
            holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (isTodayFragment()){
                        new AlertDialog.Builder(context)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are you sure?")
                                .setMessage("Are you sure to delete this item from the list?")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //MainActivity.db.execSQL("DELETE FROM Place WHERE lat =\'" + MainActivity.data.get(position).lat + "\' and lng = \'" + MainActivity.data.get(position).lng + "\' and address = \'" + MainActivity.data.get(position).address + "\';");

                                        TodaysTask.list.remove(position);
                                        TodaysTask.adapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                        return true;

                    } else if (isEveryFragment()){
                        new AlertDialog.Builder(context)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are you sure?")
                                .setMessage("Are you sure to delete this item from the list?")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //MainActivity.db.execSQL("DELETE FROM Place WHERE lat =\'" + MainActivity.data.get(position).lat + "\' and lng = \'" + MainActivity.data.get(position).lng + "\' and address = \'" + MainActivity.data.get(position).address + "\';");

                                        ManageTask.list.remove(position);
                                        ManageTask.adapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                        return true;
                    }
                    return false;
                }
            });
            }
        }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button todayBtn;
        Button everydayBtn;
        Button detailBtn;
        ImageView itemIcon;


        public MyViewHolder(ViewGroup itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.rowTextView);

            if (isSearch) {
                todayBtn = itemView.findViewById(R.id.addTodayBtn);
                everydayBtn = itemView.findViewById(R.id.addEverydayBtn);
                detailBtn = itemView.findViewById(R.id.detailBtn);
                itemIcon = itemView.findViewById(R.id.itemIcon_search);
            } else{
                itemIcon = itemView.findViewById(R.id.itemIcon);
            }

        }
    }
}
