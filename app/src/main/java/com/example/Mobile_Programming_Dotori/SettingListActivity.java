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
import java.util.Locale;




public class SettingListActivity extends AppCompatActivity {

    String tmp_name;
    String tmp_memo;
    String newName;

    EditText listname;
    EditText memo;


    InputStream inputStream = null;
    String line = null, result = null, data[];
    get_data task = new get_data(); //기존의 리스트 정보를 채워넣기 위한 객체
    // 설정 액티비티에서 나간 후, 프래그먼트 새로고침을 위한 객체
    private ListFragment projectFragment = new ListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settinglist); // layout 연결
        Intent intent = getIntent();
        String getname= intent.getStringExtra("listname"); // 설정을 수정할 리스트 이름을 얻어옴.
        task.execute("hean",getname); // 리스트 이름과 회원 아이디를 매개변수로 지정.

        listname = (EditText) findViewById(R.id.listname);
        memo  = (EditText) findViewById(R.id.memo);


        //Add button 클릭 이벤트
        Button add = (Button)findViewById(R.id.pAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 입력값을 저장.
                tmp_name = listname.getText().toString();
                tmp_memo = memo.getText().toString();

                // 조건 확인
                if (tmp_name.length() == 0) { // 리스트의 이름이 입력이 안된 경우
                    Toast.makeText(getApplicationContext(), "리스트의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else { // 모든 조건이 만족한 경우
                    task.cancel(true); // 리스트 정보를 가져오기 위한 AsyncTask 를 종료함
                    UpdateToDatabase("hean",tmp_name,tmp_memo, newName); // 리스트 업데이트를 위한 매개변수로 설정.
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    setContentView(R.layout.fragment_list);
                    //fragmentTransaction.replace(R.id.frame_layout, projectFragment); // 수정이 끝난 후 프래그먼트를 업데이트 시키기 위한 코드
                    fragmentTransaction.commit();
                }
            }
        });
    }

    public class get_data extends AsyncTask<String, Void, String> { // 비동기 클래스
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        } // 실행 전 하는 작업

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground (String...params){
            try {
                String id = params[0];
                String listname = params[1];
                //String memo = params[2];
                String uri = "http://13.124.77.84/projectsetting.php?PID="+id +"&PName="+listname; // Get 데이터 전송을 위한 url 리스트 아이디와 이름을 넘겨줌
                URL url = new URL(uri);
                // httpURLConnection을 통해 data를 가져온다.
                HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET"); //url 메소드를 GET로 결정

                inputStream = new BufferedInputStream(httpsURLConnection.getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("log_tag1", "Error converting result "+e.toString());
                e.printStackTrace();
            }

            // read input stream into a string
            try {
                // 요청 결과물 받기
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                // 라인으로 받아와 합침
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                inputStream.close();
                result = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // json data 분석
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(0);
                newName =  jsonObject.getString("PName"); // 기존의 리스트 이름을 저장
                // 기존 리스트 정보들을 DB에서 가져온 후 채워넣음
                listname.setText(jsonObject.getString("PName"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    }
    // 새롭게 수정된 부분을 DB에 업데이트 하는 코드
    private void UpdateToDatabase(String pid, String pName, String memo, String newname) {
        class UpdateData extends AsyncTask<String, Void, String> { // 비동기 클래스
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
            @Override
            protected String doInBackground(String... params) {
                String id = (String) params[0];
                String name = (String) params[1];
                String memo = (String) params[2];
                String newname = (String) params[3]; // 기존의  이름 ( DB에서 회원 아이디와 기존  이름을 검색 조건으로 사용하기 위해)
                String data = "pid="+ id +"&listname=" + name + "&memo=" + memo;

                try {
                    //GET 데이터 통신 사용
                    URL url = new URL("http://13.124.77.84/projectupdates.php?pid=" + id + "&listname="+ name + "&memo=" + memo);
                    // httpURLConnection을 통해 data를 가져온다.
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod("GET"); //url 메소드를 GET로 결정
                    httpURLConnection.connect();

                    // php문 응답 코드 확인하기
                    int responseStatusCode = httpURLConnection.getResponseCode();
                    Log.i("TAG", "POST response code- " + responseStatusCode);

                    InputStream inputStream;
                    if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    }
                    else{
                        inputStream = httpURLConnection.getErrorStream();
                    }
                    // 요청 결과물 받기
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    // 라인으로 받아와 합침
                    while((line = bufferedReader.readLine()) != null){
                        sb.append(line);
                    }

                    bufferedReader.close();
                    return sb.toString();


                } catch (Exception e) {

                    Log.e("ERROR", "UpdateData: Error ", e);

                    return new String("Error: " + e.getMessage());
                }
            }
        }
        UpdateData task = new UpdateData();
        task.execute(pid,pName,memo,newname);
    }

}
