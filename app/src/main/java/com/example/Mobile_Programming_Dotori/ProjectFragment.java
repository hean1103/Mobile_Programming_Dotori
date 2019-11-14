package com.example.Mobile_Programming_Dotori;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProjectFragment extends Fragment {
    final String[] LIST_MENU = {"운동 프로젝트", "과제 프로젝트", "100만원 모으기"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, null);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU);
        ListView listview = (ListView) view.findViewById(R.id.listview1) ;
        listview.setAdapter(adapter) ;
        FloatingActionButton fab = view.findViewById(R.id.fab_sub1);
        fab.setOnClickListener(new FABClickListener());

        return view ;
    }

    class FABClickListener implements  View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),NewProjectActivity.class);
            startActivity(intent);
        }
    }
}