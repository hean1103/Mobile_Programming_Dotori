package com.example.Mobile_Programming_Dotori;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ProjectFragment extends Fragment {
private PopupMenu popup;
    InputStream inputStream = null;
    String line = null, result = null, data[];
    ListView listview ;
    listViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, null);
        listview = (ListView) view.findViewById(R.id.listview1);
        adapter = new listViewAdapter();
        listview.setAdapter(adapter);

        get_data task = new get_data();
        task.execute("hean");
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

    public class get_data extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Refresh();
        }


        @Override
        protected String doInBackground (String...params){
        try {
            String id = params[0];
            String uri = "http://13.124.77.84/projectlist.php?PID="+id;
            URL url = new URL(uri);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");

            inputStream = new BufferedInputStream(httpsURLConnection.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("log_tag1", "Error converting result "+e.toString());
            e.printStackTrace();
        }

        // read input stream into a string
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            inputStream.close();
            result = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Parse json data
        try {

            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;
            data = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                data[i] = jsonObject.getString("PName"); // column name
            }

            for(int k = 0 ; k < data.length ; k++) {

                adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.menu),
                data[k], "Account Circle Black 36dp") ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    }
//
//    private void Refresh() {
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.detach(this).attach(this).commit();
//    }
}