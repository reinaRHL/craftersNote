package com.mp.rena.craftersnote;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordSales extends Fragment {


    private ArrayList<Item> list = new ArrayList<>();
    private RecyclerView rv;
    private MyAdapter adapter;

    public RecordSales() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_record_sales, container, false);

        rv = rootView.findViewById(R.id.recyclerViewRecord);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyAdapter(this.getContext(), list);
        rv.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);

        return rootView;
    }

}
