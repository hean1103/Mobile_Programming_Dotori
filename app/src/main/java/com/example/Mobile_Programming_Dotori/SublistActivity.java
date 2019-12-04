package com.example.Mobile_Programming_Dotori;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;

public class SublistActivity extends AppCompatActivity {

    public String pname;
    public String pid;
    public String aaa;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Log" , "여기는 sublistActivity.java에요 ㅡㅡㅡㅡㅡㅡㅡ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sublist);

        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        pname = intent.getStringExtra("pname");

        Log.i("Log" , "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡpid :" + pid);
        Log.i("Log" , "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡpname :" + pname);

        get_data task = new get_data();
        try {
            //String myChar에 이미지 이름을 받아옴
            aaa = task.execute(pid, pname).get();
            Log.i("tag", aaa);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String str[] = aaa.split(","); //각 리스트들이 리스트이름, 체크박스 형태로 되어있음

        ArrayList<String> listName = new ArrayList<>();
        ArrayList<String> checkBox = new ArrayList<>();

        for(int i=0; i<str.length; i++) {
            if(i%2 == 0) {
                listName.add(str[i]);
            }
            else
                checkBox.add(str[i]);
        }

//        for (int index = 0; index < listName.size(); index++) {
//            Log.i("ListName :----------",listName.get(index) );
//        }

        listView = (ListView)findViewById(R.id.listview);
        listAdapter adapter = new listAdapter(getApplicationContext(), 0, listName);
        listView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab_sub1); // 새로운 프로젝트 추가를 위한 + 버튼
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewListActivity.class);
                intent.putExtra("pid",pid);
                intent.putExtra("pname",pname);
                startActivity(intent);
            }
        });
    }


    private class listAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public listAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }

            TextView textView = (TextView)v.findViewById(R.id.textView);

            textView.setText(items.get(position));

            CheckBox listCheck = (CheckBox)v.findViewById(R.id.listCheck);

            //checkBox가 1이면 0으로, 0이면 1로 바꿈
            listCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCheck task = new updateCheck();
                    task.execute(pid, pname,items.get(position));
                }
            });
            return v;
        }
    }

    //리스트들을 얻기 위한 함수
    public class get_data extends AsyncTask<String, Void, String> {  // 비동기 클래스
        protected void onPreExecute() { // 실행전 수행되는 함수
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                String id = params[0];
                String PName = params[1];
                Log.i("PName : ", PName);

                String urlPath = "http://13.124.77.84/getprojectlist.php?PID=" + id + "&PName=" + PName; // 회원 아이디와 프로젝트이름을 변수로 php에 넘겨줌 디비 php 경로

                URL url = new URL(urlPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");  //GET 방식을 사용함 (default 방식)
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.connect();

                int responseStatusCode = conn.getResponseCode();
                Log.d(TAG, "response code : " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                    Log.i("http상태 코드 : " , "HTTP_OK");
                }
                else{
                    inputStream = conn.getErrorStream();
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

            } catch (Exception e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("tag", result);
        }
    }

    public class updateCheck extends AsyncTask<String, Void, String> {  // 비동기 클래스
        protected void onPreExecute() { // 실행전 수행되는 함수
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                String PID = params[0];
                String PName = params[1];
                String ListName = params[2];
                Log.i("PName : ", PName);

                String urlPath = "http://13.124.77.84/updateCheck.php?PID=" + PID + "&PName=" + PName + "&ListName=" + ListName; // 회원 아이디와 프로젝트이름을 변수로 php에 넘겨줌 디비 php 경로

                URL url = new URL(urlPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");  //GET 방식을 사용함 (default 방식)
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.connect();

                int responseStatusCode = conn.getResponseCode();
                Log.d(TAG, "response code : " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                    Log.i("http상태 코드 : " , "HTTP_OK");
                }
                else{
                    inputStream = conn.getErrorStream();
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

            } catch (Exception e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("tag", result);
        }
    }

}
