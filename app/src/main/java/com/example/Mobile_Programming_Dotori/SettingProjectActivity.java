package com.example.Mobile_Programming_Dotori;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import java.io.OutputStream;
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

    Calendar myCalendar = Calendar.getInstance();
    InputStream inputStream = null;
    String line = null, result = null, data[];
    get_data task = new get_data();
    private ProjectFragment projectFragment = new ProjectFragment();

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
        setContentView(R.layout.activity_settingproject);
        Intent intent = getIntent();
        String getname= intent.getStringExtra("pname");
        task.execute("hean",getname);

        pname = (EditText) findViewById(R.id.pname);
        dateFrom = (EditText) findViewById(R.id.dateFrom);
        dateto = (EditText) findViewById(R.id.dateTo);
        fID  = (EditText) findViewById(R.id.fId);

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SettingProjectActivity.this, listener_from, 2019, 5, 24);
                dialog.show();
            }
        });
        dateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SettingProjectActivity.this, listener_to, 2019, 5, 24);
                dialog.show();
            }
        });
        //Add button 클릭 이벤트
        Button add = (Button)findViewById(R.id.pAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmp_name = pname.getText().toString();
                tmp_from = dateFrom.getText().toString();
                tmp_to = dateto.getText().toString();
                tmp_fid = fID.getText().toString();
                if (tmp_name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "프로젝트의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (tmp_from.length() == 0) {
                    Toast.makeText(getApplicationContext(), "시작 일자를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (tmp_to.length() == 0) {
                    Toast.makeText(getApplicationContext(), "종료 일자를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    task.cancel(true);
                    UpdateToDatabase("hean",tmp_name,tmp_from,tmp_to,newName);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    setContentView(R.layout.activity_main);
                    fragmentTransaction.replace(R.id.frame_layout, projectFragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    public class get_data extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground (String...params){
            try {
                String id = params[0];
                String pname = params[1];
                String uri = "http://13.124.77.84/projectsetting.php?PID="+id +"&PName="+pname;
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
                jsonObject = jsonArray.getJSONObject(0);
                newName =  jsonObject.getString("PName");
                pname.setText(jsonObject.getString("PName"));
                dateFrom.setText(jsonObject.getString("DateFrom"));
                dateto.setText(jsonObject.getString("DateTo"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    }
    private void UpdateToDatabase(String pid,String pName, String datefrom , String dateto, String newname) {
        class UpdateData extends AsyncTask<String, Void, String> {
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
                String newname = (String) params[4];
                String data = "pid="+ id +"&pname=" + name + "&datefrom=" + datefrom + "&dateto=" + dateto;
                Log.i("CHECK========", id+"==" + name + "== "+ datefrom + "==" + dateto);
                try {
                    URL url = new URL("http://13.124.77.84/projectupdates.php?pid=" + id + "&pname="+ name + "&datefrom=" + datefrom + "&dateto=" + dateto + "&newname="+newname);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod("GET");
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

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while((line = bufferedReader.readLine()) != null){
                        sb.append(line);
                    }

                    bufferedReader.close();
                    Log.i("sbsbsb==== ",sb.toString());
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