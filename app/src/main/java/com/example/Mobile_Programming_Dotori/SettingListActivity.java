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

    String tmp_listname;

    String tmp_memo;
    String newName;

    EditText listname;
    EditText memo;

    public String getpname;
    public String getid;

    InputStream inputStream = null;
    String line = null, result = null, data[];
    get_data task = new get_data(); //기존의 리스트 정보를 채워넣기 위한 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settinglist); // layout 연결
        Intent intent = getIntent();
        String getname= intent.getStringExtra("listname"); // 설정을 수정할 리스트 이름을 얻어옴.
        getid= intent.getStringExtra("id");
        getpname= intent.getStringExtra("pname"); // @@@@@@@@@@@@
        task.execute(getid, getpname,getname); // 회원 아이디, 프로젝트 이름과 리스트 이름을 매개변수로 지정.  //@@@@@@@@@@@@

        listname = (EditText) findViewById(R.id.listname);
        memo  = (EditText) findViewById(R.id.memo);

        //Add button 클릭 이벤트
        Button add = (Button)findViewById(R.id.listAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 입력값을 저장.
                tmp_listname = listname.getText().toString();
                tmp_memo = memo.getText().toString();

                // 조건 확인
                if (tmp_listname.length() == 0) { // 리스트의 이름이 입력이 안된 경우
                    Toast.makeText(getApplicationContext(), "리스트의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else { // 모든 조건이 만족한 경우
                    task.cancel(true); // 프로젝트 정보를 가져오기 위한 AsyncTask 를 종료함
                    UpdateToDatabase(getid,getpname, tmp_listname, tmp_memo, newName); // 프로젝트 업데이트를 위한 매개변수로 설정.

                }
            }
        });
    }

    public class get_data extends AsyncTask<String, Void, String> { // 비동기 클래스
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        } // 실행 전 하는 작업

        @SuppressLint("WrongThread") //지우기
        @Override
        protected String doInBackground (String...params){
            try {
                String PID = params[0];
                String PName = params[1];
                String ListName = params[2];
                String Memo = params[3];
                String uri = "http://13.124.77.84/listsetting.php?PID="+PID +"&PName="+PName +"&ListName="+ListName + "&Memo=" +Memo; // Get 데이터 전송을 위한 url 리스트 아이디와 이름을 넘겨줌 //@@@@@@@@@@
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
                newName =  jsonObject.getString("ListName"); // 기존의 리스트 이름을 저장
                // 기존 리스트 정보들을 DB에서 가져온 후 채워넣음
                listname.setText(jsonObject.getString("ListName"));
                memo.setText(jsonObject.getString("Memo"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    }
    // 새롭게 수정된 부분을 DB에 업데이트 하는 코드
    private void UpdateToDatabase(String PID, String PName, String ListName, String Memo, String NewName) { // 여기만 NewName
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
                String PID = (String) params[0];
                String PName = (String) params[1];
                String ListName = (String) params[2];
                String Memo = (String) params[3];
                String newName = (String) params[4]; // 기존의  이름 ( DB에서 회원 아이디와 기존  이름을 검색 조건으로 사용하기 위해)
                String data = "PID="+ PID +"&PName=" + PName +"&ListName=" + ListName +"&Memo=" + Memo;

                try {
                    //GET 데이터 통신 사용
                    URL url = new URL("http://13.124.77.84/listupdates.php?PID=" + PID + "&PName="+ PName + "&ListName=" + ListName + "&Memo=" + Memo + "&newName="+newName); //@@@@@@@@@@@@@
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
        task.execute(PID,PName,ListName, Memo ,NewName);
    }

}
