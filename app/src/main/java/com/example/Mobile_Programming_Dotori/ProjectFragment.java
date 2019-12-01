package com.example.Mobile_Programming_Dotori;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ProjectFragment extends Fragment {
private PopupMenu popup;
    InputStream inputStream = null;
    String line = null, result = null, data[];
    ListView listview ;
    listViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
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
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground (String...params){
        try {
            String id = params[0];
            String uri = "http://13.124.77.84/projectlist.php?PID="+id;
            URL url = new URL(uri);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setReadTimeout(5000);
            httpsURLConnection.setConnectTimeout(5000);
            httpsURLConnection.connect();

            int responseStatusCode = httpsURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpsURLConnection.getInputStream();
                Log.i("http상태 코드 : " , "HTTP_OK");
            }
            else{
                inputStream = httpsURLConnection.getErrorStream();
                Log.i("http상태 코드 : " , "NOT HTTP_OK");
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null){
                Log.i("php 내용 가져오기 : ", line);
                sb.append(line);
            }

            bufferedReader.close();
            Log.i("실행 결과 : ", sb.toString());
            return sb.toString().trim();

        } catch (MalformedURLException e) {
            return e.getMessage();
        } catch (IOException e) {
            Log.e("log_tag1", "Error converting result "+e.toString());
            return e.getMessage();
        }
    }


        @Override
        protected void onPostExecute(String result){
            adapter = new listViewAdapter();
            listview.setAdapter(adapter);
            // Parse json data
            try {

                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                data = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    data[i] = jsonObject.getString("PName"); // column name
                    Log.i("php 내용 가져오기============== : ", data[i]);
                }

                for(int k = 0 ; k < data.length ; k++) {
                    adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.menu),
                            data[k], "Account Circle Black 36dp") ;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}