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


public class SettingProjectActivity extends AppCompatActivity {

    String tmp_name;
    String tmp_from;
    String tmp_to;
    String tmp_fid;
    String newName;

    EditText pname;
    EditText dateFrom;
    EditText dateto;
    EditText fID;
    public String getid;
    public int year;
    public int month;
    public int days;


    Calendar myCalendar = Calendar.getInstance(); // DatePicker를 사용하기 위해 달력 선언
    InputStream inputStream = null;
    String line = null, result = null, data[];
    get_data task = new get_data(); //기존의 프로젝트 정보를 채워넣기 위한 객체
    // 설정 액티비티에서 나간 후, 프래그먼트 새로고침을 위한 객체
    private ProjectFragment projectFragment = new ProjectFragment();

    // 날짜 지정 부분 클릭시 DatePicker 실행
    private  DatePickerDialog.OnDateSetListener listener_from = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            dateFrom.setText(sdf.format(myCalendar.getTime())); //datefrom 칸에 채워넣기
        }
    };

    private  DatePickerDialog.OnDateSetListener listener_to = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            dateto.setText(sdf.format(myCalendar.getTime())); //dateto 칸에 채워넣기
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingproject); // layout 연결
        Intent intent = getIntent();
        String getname= intent.getStringExtra("pname"); // 설정을 수정할 프로젝트 이름을 얻어옴.
        getid= intent.getStringExtra("pid"); // 설정을 수정할 프로젝트 이름을 얻어옴.
        task.execute(getid,getname); // 프로젝트 이름과 회원 아이디를 매개변수로 지정.
        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);
        days = myCalendar.get(Calendar.DATE);


        pname = (EditText) findViewById(R.id.pname);
        dateFrom = (EditText) findViewById(R.id.dateFrom);
        dateto = (EditText) findViewById(R.id.dateTo);
        fID  = (EditText) findViewById(R.id.fId);

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SettingProjectActivity.this, listener_from, year, month, days); // Datepicker 기본 날짜 지정
                dialog.show();
            }
        });
        dateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SettingProjectActivity.this, listener_to, year, month, days);
                dialog.show();
            }
        });

        //Add button 클릭 이벤트
        Button add = (Button)findViewById(R.id.listAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 입력값을 저장.
                tmp_name = pname.getText().toString();
                tmp_from = dateFrom.getText().toString();
                tmp_to = dateto.getText().toString();
                tmp_fid = fID.getText().toString();

                // 조건 확인
                if (tmp_name.length() == 0) { // 프로젝트의 이름이 입력이 안된 경우
                    Toast.makeText(getApplicationContext(), "프로젝트의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (tmp_from.length() == 0) { // 시작 일자가 입력이 안된 경우
                    Toast.makeText(getApplicationContext(), "시작 일자를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (tmp_to.length() == 0) { // 종료 일자가 입력이 안된 경우
                    Toast.makeText(getApplicationContext(), "종료 일자를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else { // 모든 조건이 만족한 경우
                    task.cancel(true); // 프로젝트 정보를 가져오기 위한 AsyncTask 를 종료함
                    UpdateToDatabase(getid,tmp_name,tmp_from,tmp_to,newName); // 프로젝트 업데이트를 위한 매개변수로 설정.
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    setContentView(R.layout.activity_main);
                    fragmentTransaction.replace(R.id.frame_layout, projectFragment.newInstance(getid)); // 수정이 끝난 후 프래그먼트를 업데이트 시키기 위한 코드
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

        @Override
        protected String doInBackground (String...params){
            try {
                String id = params[0];
                String pname = params[1];
                String uri = "http://13.124.77.84/projectsetting.php?PID="+id +"&PName="+pname; // Get 데이터 전송을 위한 url 프로젝트 아이디와 이름을 넘겨줌
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
                newName =  jsonObject.getString("PName"); // 기존의 프로젝트 이름을 저장
                // 기존 프로젝트 정보들을 DB에서 가져온 후 채워넣음
                pname.setText(jsonObject.getString("PName"));
                dateFrom.setText(jsonObject.getString("DateFrom"));
                dateto.setText(jsonObject.getString("DateTo"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    }
    // 새롭게 수정된 부분을 DB에 업데이트 하는 코드
    private void UpdateToDatabase(String pid,String pName, String datefrom , String dateto, String newname) {
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
                String datefrom = (String) params[2];
                String dateto = (String) params[3];
                String newname = (String) params[4]; // 기존의 프로젝트 이름 ( DB에서 회원 아이디와 기존 프로젝트 이름을 검색 조건으로 사용하기 위해)
                String data = "pid="+ id +"&pname=" + name + "&datefrom=" + datefrom + "&dateto=" + dateto;

                try {
                    //GET 데이터 통신 사용
                    URL url = new URL("http://13.124.77.84/projectupdates.php?pid=" + id + "&pname="+ name + "&datefrom=" + datefrom + "&dateto=" + dateto + "&newname="+newname);
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
        task.execute(pid,pName,datefrom,dateto,newname);
    }

}