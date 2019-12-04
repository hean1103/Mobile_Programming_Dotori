package com.example.Mobile_Programming_Dotori;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;


public class SettingListActivity extends AppCompatActivity {

    EditText editName;
    EditText editMemo;

    public String listname;
    public String pname;
    public String pid;

    String tmp_listname;
    String tmp_memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settinglist); // layout 연결
        Intent intent = getIntent();
        listname= intent.getStringExtra("listName"); // 리스트 이름 전달받음
        pid= intent.getStringExtra("pid"); // 사용자 이름 전달받음
        pname = intent.getStringExtra("pname"); // 프로젝트 이름 전달받음

        editName = (EditText) findViewById(R.id.listname);
        editMemo  = (EditText) findViewById(R.id.memo);

        //Add button 클릭 이벤트
        Button add = (Button)findViewById(R.id.listAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 입력값을 저장.
                tmp_listname = editName.getText().toString();
                tmp_memo = editMemo.getText().toString();

                // 조건 확인
                if (tmp_listname.length() == 0) { // 리스트의 이름이 입력이 안된 경우
                    Toast.makeText(getApplicationContext(), "리스트의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else { // 모든 조건이 만족한 경우
                    settingList task = new settingList(); //기존의 리스트 정보를 채워넣기 위한 객체
                    try {
                        task.execute(pid, pname, listname, tmp_listname, tmp_memo);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public class settingList extends AsyncTask<String, Void, String> { // 비동기 클래스
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        } // 실행 전 하는 작업

        @Override
        protected String doInBackground (String...params) {
            try {
                String PID = params[0];
                String PName = params[1];
                String ListName = params[2];
                String NewListName = params[3];
                String Memo = params[4];

                String uri = "http://13.124.77.84/listsetting.php?PID=" + PID + "&PName=" + PName + "&ListName=" + ListName + "&NewListName=" + NewListName + "&Memo=" + Memo; // Get 데이터 전송을 위한 url 리스트 아이디와 이름을 넘겨줌 //@@@@@@@@@@
                URL url = new URL(uri);
                // httpURLConnection을 통해 data를 가져온다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");  //GET 방식을 사용함 (default 방식)
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.connect();

                int responseStatusCode = conn.getResponseCode();
                Log.d(TAG, "response code : " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                    Log.i("http상태 코드 : ", "HTTP_OK");
                } else {
                    inputStream = conn.getErrorStream();
                    Log.i("http상태 코드 : ", "NOT HTTP_OK");
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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
    }


}
