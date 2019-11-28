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


public class NewProjectActivity extends AppCompatActivity {

    String tmp_name;
    String tmp_from;
    String tmp_to;
    String tmp_fid;

    EditText pname;
    EditText dateFrom;
    EditText dateto;
    EditText fID;

    Calendar myCalendar = Calendar.getInstance();

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
        setContentView(R.layout.activity_newproject);

        pname = (EditText) findViewById(R.id.pname);
        dateFrom = (EditText) findViewById(R.id.dateFrom);
        dateto = (EditText) findViewById(R.id.dateTo);
        fID  = (EditText) findViewById(R.id.fId);


        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewProjectActivity.this, listener_from, 2019, 5, 24);
                dialog.show();
            }
        });
        dateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewProjectActivity.this, listener_to, 2019, 5, 24);
                dialog.show();
            }
        });
        //Add button and Add button event
        Button add = (Button)findViewById(R.id.pAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmp_name = pname.getText().toString();
                tmp_from = dateFrom.getText().toString();
                tmp_to = dateto.getText().toString();
//                tmp_fid = fID.getText().toString();

                if (tmp_name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "프로젝트의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (tmp_from.length() == 0) {
                    Toast.makeText(getApplicationContext(), "시작 일자를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (tmp_to.length() == 0) {
                    Toast.makeText(getApplicationContext(), "종료 일자를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    insertoToDatabase("hean",tmp_name,tmp_from,tmp_to);
                }
            }
        });

    }
    private void insertoToDatabase(String pid,String pName, String DFrom, String Dto) {
        class InsertData extends AsyncTask<String, Void, String> {
            private ProjectFragment projectFragment = new ProjectFragment();
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NewProjectActivity.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {
                //                    String Id = (String) params[0];
                    String id = (String) params[0];
                    String name = (String) params[1];
                    String From = (String) params[2];
                    String DTo = (String) params[3];
                    String data = "pid="+ id +"&pname=" + name + "&datafrom=" + From + "&datato=" + DTo;
                try {

                    URL url = new URL("http://13.124.77.84/getProject.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.connect();

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(data.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();


                    int responseStatusCode = httpURLConnection.getResponseCode();
                    Log.d("TAG", "POST response code - " + responseStatusCode);

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
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, projectFragment);
                    fragmentTransaction.commit();

                    return sb.toString();


                } catch (Exception e) {

                    Log.d("ERROR", "InsertData: Error ", e);

                    return new String("Error: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(pid,pName,DFrom, Dto);
}

}


