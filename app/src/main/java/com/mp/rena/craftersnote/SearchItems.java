package com.mp.rena.craftersnote;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchItems extends Fragment {

    static ArrayList<Item> list = new ArrayList<>();
    private RecyclerView rv;
    static MyAdapter adapter;
    EditText searchWindow;
    Button searchBtn;

    public class DownloadResults extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                String result="";
                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    public SearchItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_items, container, false);

        rv = rootView.findViewById(R.id.recyclerViewSearch);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyAdapter(this.getContext(), list);
        rv.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);

        searchWindow = rootView.findViewById(R.id.title_search);
        searchBtn = rootView.findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create download instance then pass url to add contents to the list
                DownloadResults download = new DownloadResults();
                String itemList="";
                String urlString = searchWindow.getText().toString();
                try {
                    itemList = download.execute("https://xivapi.com/search?indexes=recipe&string=" + urlString).get();
                    JSONObject itemListJSON = new JSONObject(itemList);
                    String results = itemListJSON.getString("Results");
                    JSONArray resultsArr = new JSONArray(results);
                    list.clear();
                    for (int i=0; i< resultsArr.length(); i++){
                        JSONObject recipe = resultsArr.getJSONObject(i);
                        String itemName = recipe.getString("Name");
                        int recipeID = Integer.parseInt(recipe.getString("ID"));
                        String icon = recipe.getString("Icon");
                        String url = recipe.getString("Url");
                        Item item = new Item(itemName, recipeID, icon, url);
                        list.add(item);
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

}
