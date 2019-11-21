package com.example.Mobile_Programming_Dotori;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ProjectFragment extends Fragment {
//    final String[] LIST_MENU = {"운동 프로젝트", "과제 프로젝트", "100만원 모으기"};
private PopupMenu popup;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, null);
        ListView listview ;
        listViewAdapter adapter;
        adapter = new listViewAdapter() ;
        listview = (ListView) view.findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.menu),
                "운동 프로젝트", "Account Circle Black 36dp") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.menu),
                "과제 프로젝트", "Assignment Ind Black 36dp") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.menu),
                "100만원 모으기 프로젝트", "Assignment Ind Black 36dp") ;
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