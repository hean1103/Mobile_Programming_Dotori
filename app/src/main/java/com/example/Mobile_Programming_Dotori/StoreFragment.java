package com.example.Mobile_Programming_Dotori;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class StoreFragment extends Fragment {
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_store, container, false);
        listView = (ListView)v.findViewById(R.id.listview);

        ArrayList<String> items = new ArrayList<>();
        items.add("cat");
        items.add("dog");
        items.add("ducky");
        items.add("penguin");
        items.add("turtle");

        CustomAdapter adapter = new CustomAdapter(getActivity(), 0, items);
        listView.setAdapter(adapter);

        return v;
    }


    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.store_itemlist, null);
            }

            // ImageView 인스턴스
            ImageView imageView = (ImageView)v.findViewById(R.id.imageView);

            // 리스트뷰의 아이템에 이미지를 변경한다.
            if("cat".equals(items.get(position)))
                imageView.setImageResource(R.drawable.cat);
            else if("dog".equals(items.get(position)))
                imageView.setImageResource(R.drawable.dog);
            else if("ducky".equals(items.get(position)))
                imageView.setImageResource(R.drawable.ducky);
            else if("penguin".equals(items.get(position)))
                imageView.setImageResource(R.drawable.penguin);
            else if("turtle".equals(items.get(position)))
                imageView.setImageResource(R.drawable.turtle);


            TextView textView = (TextView)v.findViewById(R.id.textView);
            textView.setText(items.get(position));

            final String text = items.get(position);

            return v;
        }
    }

}