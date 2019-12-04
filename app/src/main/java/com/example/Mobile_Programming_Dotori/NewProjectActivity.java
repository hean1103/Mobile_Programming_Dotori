package com.example.Mobile_Programming_Dotori;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;


public class NewProjectActivity extends AppCompatActivity {

    String tmp_name;
    String tmp_from;
    String tmp_to;

    EditText pname;
    EditText dateFrom;
    EditText dateto;
    EditText fID;

    public String id;
    public int year;
    public int month;
    public int days;

    private ProjectFragment projectFragment = new ProjectFragment();

    Calendar myCalendar = Calendar.getInstance();
    // 달력을 통한 날짜 설정 기능
    private  DatePickerDialog.OnDateSetListener listener_from = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            dateFrom.setText(sdf.format(myCalendar.getTime()));
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

            dateto.setText(sdf.format(myCalendar.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproject); // layout 연결
        //로그인 한 아이디 값 가져오기
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);
        days = myCalendar.get(Calendar.DATE);

        //EditText에 입력된 값들을 가져옴
        pname = (EditText) findViewById(R.id.pname);
        dateFrom = (EditText) findViewById(R.id.dateFrom);
        dateto = (EditText) findViewById(R.id.dateTo);
        fID  = (EditText) findViewById(R.id.fId);
        //Datepicker를 통한 날짜 설정
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewProjectActivity.this, listener_from, year, month, days); // 기본 날짜 설정
                dialog.show();
            }
        });
        dateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewProjectActivity.this, listener_to, year, month, days);
                dialog.show();
            }
        });
        //Add 버튼을 눌렀을 경우
        Button add = (Button)findViewById(R.id.listAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 저장된 정보들을 가져옴
                tmp_name = pname.getText().toString();
                tmp_from = dateFrom.getText().toString();
                tmp_to = dateto.getText().toString();

                if (tmp_name.length() == 0) { // 프로젝트의 이름이 입력되지 않았을 경우
                    Toast.makeText(getApplicationContext(), "프로젝트의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (tmp_from.length() == 0) { // 시작 날짜가 입력되지 않았을 경우
                    Toast.makeText(getApplicationContext(), "시작 일자를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (tmp_to.length() == 0) { // 종료 날짜가 입력되지 않았을 경우
                    Toast.makeText(getApplicationContext(), "종료 일자를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 새로운 프로젝트 정보를 DB에 넣기 위한 기능 ( ID와 프로젝트 이름, 시작 날짜, 종료 날짜를 매개변수로 설정)
                    insertoToDatabase(id,tmp_name,tmp_from,tmp_to);
                    finish();
                }
            }
        });

    }
    //서버의 PHP문을 사용하여 DB에 데이터를 삽입하는 함수
    private void insertoToDatabase(String pid,String pName, String DFrom, String Dto) {
        class InsertData extends AsyncTask<String, Void, String> { // 비동기 클래스
            ProgressDialog loading; // 로딩 작업
            @Override
            protected void onPreExecute() { // 실행전 수행하는 코드
                super.onPreExecute();
                loading = ProgressDialog.show(NewProjectActivity.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();  // 로딩 종료
                // php문에서 넘어온 결과를 토스트 메세지를 통해 화면에 띄움
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {
                    String id = (String) params[0];
                    String name = (String) params[1];
                    String From = (String) params[2];
                    String DTo = (String) params[3];
                    // 회원 아이디와 프로젝트 이름 , 시작 날짜와 종료날짜를 변수로 넘겨줌
                    String data = "pid="+ id +"&pname=" + name + "&datafrom=" + From + "&datato=" + DTo;
                try {
                    //서버의 php문에 연결
                    URL url = new URL("http://13.124.77.84/getProject.php");
                    // httpURLConnection을 통해 data를 가져온다.
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod("POST"); // Post 데이터 통신을 사용
                    httpURLConnection.connect();

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(data.getBytes("UTF-8"));
                    outputStream.flush(); // 버퍼링된 출력 바이트 실행
                    outputStream.close();

                    // php문 응답 코드 확인하기
                    int responseStatusCode = httpURLConnection.getResponseCode();
                    Log.d("TAG", "POST response code - " + responseStatusCode);

                    InputStream inputStream;
                    // 서버로 부터의 응답코드가 200인 경우
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
                    // 라인을 받아와 합침
                    while((line = bufferedReader.readLine()) != null){
                        sb.append(line);
                    }


                    bufferedReader.close();
                    // onPseExecute로 결과값 전달
                    return sb.toString();


                } catch (Exception e) {

                    Log.d("ERROR", "InsertData: Error ", e);

                    return new String("Error: " + e.getMessage());
                }
            }
        }
        //DB 삽입 실행 객체
        InsertData task = new InsertData();
        task.execute(pid,pName,DFrom, Dto);
}

}


