package com.example.Mobile_Programming_Dotori;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Locale;


public class NewListActivity extends AppCompatActivity {

    String tmp_name;
    String tmp_memo;

    EditText listname;
    EditText memo;

    public String PID;
    public String PName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlist); // layout 연결

        Intent intent = getIntent(); /*데이터 수신*/
        PID = intent.getExtras().getString("pid");
        PName = intent.getExtras().getString("pname");

        //EditText에 입력된 값들을 가져옴
        listname = (EditText) findViewById(R.id.listname);
        memo  = (EditText) findViewById(R.id.memo);

        //Add 버튼을 눌렀을 경우
        Button add = (Button)findViewById(R.id.listAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 저장된 정보들을 가져옴
                tmp_name = listname.getText().toString();
                tmp_memo = memo.getText().toString();

                if (tmp_name.length() == 0) { // 리스트의 이름이 입력되지 않았을 경우
                    Toast.makeText(getApplicationContext(), "리스트의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // 새로운 리스트 정보를 DB에 넣기 위한 기능 (매개변수로 설정)
                    insertoToDatabase(PID, PName, tmp_name, tmp_memo);
                    finish();
                }
            }
        });

    }
    //서버의 PHP문을 사용하여 DB에 데이터를 삽입하는 함수
    private void insertoToDatabase(String PID, String PName, String ListName, String Memo) {
        class InsertData extends AsyncTask<String, Void, String> { // 비동기 클래스
            ProgressDialog loading; // 로딩 작업
            @Override
            protected void onPreExecute() { // 실행전 수행하는 코드
                super.onPreExecute();
                loading = ProgressDialog.show(NewListActivity.this, "Please Wait", null, true, true);
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
                String PID = (String) params[0];
                String PName = (String) params[1];
                String ListName = (String) params[2];
                String Memo = (String) params[3];
                // 회원 아이디와 프로젝트 이름, 리스트 이름, 메모를 변수로 넘겨줌

                try {
                    //서버의 php문에 연결
                    String urlpath = "http://13.124.77.84/listupdate.php?PID="+PID +"&PName=" + PName + "&ListName=" + ListName +"&Memo=" + Memo; // 회원 아이디를 변수로 php에 넘겨줌

                    URL url = new URL(urlpath);

                    // httpURLConnection을 통해 data를 가져온다.
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

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
        task.execute(PID, PName, ListName, Memo);
    }

}
