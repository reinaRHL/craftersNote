package com.mp.rena.craftersnote;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import static com.mp.rena.craftersnote.TodaysTask.list;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageTask extends Fragment {

    public ManageTask() {
        // Required empty public constructor
    }

    static ArrayList<Item> list = new ArrayList<>();
    private RecyclerView rv;
    static MyAdapter adapter;
    ImageButton addBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_manage_task, container, false);

        rv = rootView.findViewById(R.id.recyclerViewManage);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyAdapter(this.getContext(), list);
        rv.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);

        addBtn = rootView.findViewById(R.id.imageButton2);
        addBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.bottomNavigation.setSelectedItemId(R.id.action_search);
            }
        });

        return rootView;

    }

}
