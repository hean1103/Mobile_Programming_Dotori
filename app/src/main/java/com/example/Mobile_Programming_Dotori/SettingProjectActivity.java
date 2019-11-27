package com.example.Mobile_Programming_Dotori;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;


public class SettingProjectActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_settingproject);

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
        //Add button and Add button event
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
                }
            }
        });

    }

}