package com.mp.rena.craftersnote;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
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
                    MainActivity.db.insertToday(item);
                }
            });
            holder.everydayBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item item = list.get(position);
                    MainActivity.db.insertToday(item);
                    MainActivity.db.insertEveryday(item);
                }
            });
            holder.detailBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadResults download = new DownloadResults();
                    Item item = new Item("", 0, "", "", "" );
                    AlertDialog.Builder builder;
                    final AlertDialog alertDialog;
                    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.dialog_custom_layout, null);

                    if (list.get(position).urlType.equals("Recipe")){
                        try {
                            String result = download.execute("https://xivapi.com" + list.get(position).url + "?key=3e1f649be24c4f8bb33b9a42").get();
                            JSONObject resultJSON = new JSONObject(result);
                            String url = resultJSON.getString("Url");
                            int id = Integer.parseInt(resultJSON.getString("ID"));

                            JSONObject itemResultJSON = resultJSON.getJSONObject("ItemResult");
                            String icon = itemResultJSON.getString("Icon");
                            String desc = itemResultJSON.getString("Description");
                            desc = desc.substring(0, desc.indexOf(".")+1);
                            String name = itemResultJSON.getString("Name");

                            JSONObject classJobJSON = resultJSON.getJSONObject("ClassJob");
                            String job = classJobJSON.getString("NameEnglish");

                            JSONObject recipeLevelTableJSON = resultJSON.getJSONObject("RecipeLevelTable");
                            String jobLevel = recipeLevelTableJSON.getString("ClassJobLevel");

                            String urlType = url.split("/")[0];
                            item = new Item(name, id, icon, url, urlType);
                            item.description = desc;
                            item.job = job;
                            item.jobLevel = jobLevel;
                            Item.reqMaterialList.clear();
                            for (int i=0; i<10; i++){
                                int amount = Integer.parseInt(resultJSON.getString("AmountIngredient" + i));
                                if (amount > 0){
                                    String materialName = resultJSON.getJSONObject("ItemIngredient" + i).getString("Name");
                                    String materialUrlType = resultJSON.getString("ItemIngredient" + i + "Target");
                                    int materialID = Integer.parseInt(resultJSON.getString("ItemIngredient" + i + "TargetID"));
                                    String materialIcon = resultJSON.getJSONObject("ItemIngredient" + i).getString("Icon");
                                    String materialUrl = "/" + materialUrlType + "/" + materialID;
                                    Item materialItem = new Item(materialName, materialID, materialIcon, materialUrl, materialUrlType);
                                    materialItem.quantity = amount;
                                    Item.reqMaterialList.add(materialItem);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (list.get(position).urlType.equals("Item")){
                        try {
                            String result = download.execute("https://xivapi.com" + list.get(position).url).get();
                            JSONObject resultJSON = new JSONObject(result);
                            String url = resultJSON.getString("Url");
                            int id = Integer.parseInt(resultJSON.getString("ID"));
                            String icon = resultJSON.getString("Icon");
                            String name = resultJSON.getString("Name");
                            String desc = resultJSON.getString("Description");
                            String jobLevel = resultJSON.getString("LevelItem");
                            String job = "";
                            String urlType = url.split("/")[0];
                            item = new Item(name, id, icon, url, urlType);
                            item.description = desc;
                            item.job = job;
                            item.jobLevel = jobLevel;

                            TextView materialLabel = layout.findViewById(R.id.materialLabel);
                            materialLabel.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // create dialog for details
                    builder = new AlertDialog.Builder(context);
                    builder.setView(layout);
                    alertDialog = builder.create();

                    //textviews in the dialog
                    TextView itemNameD = layout.findViewById(R.id.itemNameDetail);
                    TextView itemDescD = layout.findViewById(R.id.itemDescDetail);
                    TextView jobD = layout.findViewById(R.id.requiredClass);
                    TextView jobLevelD = layout.findViewById(R.id.requiredClassLevel);

                    itemNameD.setText(item.name);
                    itemDescD.setText(item.description);
                    jobD.setText(item.job);
                    jobLevelD.setText("Lv. " + item.jobLevel);

                    // required material list in the dialog
                    ListView listView = layout.findViewById(R.id.requiredMaterialDetail);
                    ArrayAdapter arrayAdapter = new RequiredMaterialAdapter(layout.getContext(), Item.reqMaterialList);
                    listView.setAdapter(arrayAdapter);

                    // Add buttons in the dialog
                    Button todayDBtn = layout.findViewById(R.id.addTodayDialog);
                    Button everyDBtn = layout.findViewById(R.id.addEverydayDialog);
                    todayDBtn.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.todayBtn.performClick();
                        }
                    });
                    everyDBtn.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.everydayBtn.performClick();
                        }
                    });
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
                                        MainActivity.db.deleteToday(list.get(position).name, list.get(position).id);
                                        list.remove(position);
                                        notifyDataSetChanged();
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
                                        MainActivity.db.deleteEveryDay(list.get(position).name, list.get(position).id);
                                        list.remove(position);
                                        notifyDataSetChanged();
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
                todayBtn = itemView.findViewById(R.id.addTodayBtnRM);
                everydayBtn = itemView.findViewById(R.id.addEverydayBtnRM);
                detailBtn = itemView.findViewById(R.id.detailBtn);
                itemIcon = itemView.findViewById(R.id.itemIconRM);
            } else{
                itemIcon = itemView.findViewById(R.id.itemIcon);
            }

        }
    }

    public class DownloadResults extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                InputStream in = connection.getInputStream();
                StringBuilder response = new StringBuilder(50000);
                char[] buf = new char[2048];
                int charRead;
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                while((charRead = rd.read(buf, 0, 2048)) > 0) {
                    response.append(buf, 0, charRead);
                }

                return response.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}
