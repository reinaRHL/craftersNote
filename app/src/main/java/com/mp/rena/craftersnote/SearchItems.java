package com.mp.rena.craftersnote;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchItems extends Fragment {

    static ArrayList<String> list = new ArrayList<>();;
    private RecyclerView rv;
    static MyAdapter adapter;

    public SearchItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        list.add("5");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_items, container, false);

        rv = rootView.findViewById(R.id.recyclerViewSearch);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyAdapter(this.getContext(), list);
        rv.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);

        return rootView;
    }

}
