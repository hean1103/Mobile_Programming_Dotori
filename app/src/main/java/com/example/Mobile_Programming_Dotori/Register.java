package com.example.Mobile_Programming_Dotori;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

//회원가입 구현
public class Register extends AppCompatActivity {

    //레이아웃에서 불러올 아이디값
    private EditText editTextId;
    private EditText editTextPw;
    private EditText editTextPw_check;
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextBirth;

    private AlertDialog dialog; // 다이얼로그 선언
    private boolean validate = false; //ID체크

    //연결할 ip와 php
    public static final String link = "http://13.124.77.84/Register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextId = (EditText) findViewById(R.id.new_id);
        editTextPw = (EditText) findViewById(R.id.new_pw);
        editTextPw_check = (EditText) findViewById(R.id.new_pw_check);
        editTextName = (EditText) findViewById(R.id.new_name);
        editTextPhone = (EditText) findViewById(R.id.new_phone);
        editTextBirth = (EditText) findViewById(R.id.new_birth);


        //휴대폰 번호 editText 양식 지정 (입력할때마다 양식이 변하게 함)
        editTextPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cursorComplement = s.length()-editTextPhone.getSelectionStart();
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                String phone = string.replaceAll("[^\\d]", "");

                if (!editedFlag) {

                    if (phone.length() >= 7 && !backspacingFlag) {//editText의 스트링 길이가 7이상이면 괄호 두개와 하나의 하이푼 설정

                        editedFlag = true;

                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,7) + "-" + phone.substring(7);
                        editTextPhone.setText(ans);

                        editTextPhone.setSelection(editTextPhone.getText().length()-cursorComplement);

                    } else if (phone.length() >= 3 && !backspacingFlag) { //editText의 스트링 길이가 3이상이면 괄호 두개 설정
                        editedFlag = true;
                        String ans = "(" +phone.substring(0, 3) + ") " + phone.substring(3);
                        editTextPhone.setText(ans);
                        editTextPhone.setSelection(editTextPhone.getText().length()-cursorComplement);
                    }

                } else {
                    editedFlag = false;
                }
            }
        });


        //생일 editText 양식 지정 (입력할때마다 양식이 변하게 함)
        editTextBirth.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cursorComplement = s.length()-editTextBirth.getSelectionStart();
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                String phone = string.replaceAll("[^\\d]", "");

                if (!editedFlag) {
                    if (phone.length() >= 7 && !backspacingFlag) { //editText의 스트링 길이가 7이상이면 하이푼 두개 설정

                        editedFlag = true;

                        String ans =  phone.substring(0, 4) + "-" + phone.substring(4,6) + "-" + phone.substring(6);
                        editTextBirth.setText(ans);

                        editTextBirth.setSelection(editTextBirth.getText().length()-cursorComplement);

                    } else if (phone.length() >= 4 && !backspacingFlag) { //editText의 스트링 길이가 4이상이면 하이푼 한개 설정
                        editedFlag = true;
                        String ans = phone.substring(0, 4) + "-" + phone.substring(4);
                        editTextBirth.setText(ans);
                        editTextBirth.setSelection(editTextBirth.getText().length()-cursorComplement);
                    }

                } else {
                    editedFlag = false;
                }
            }
        });

        //중복확인 버튼
        Button validateButton = (Button)findViewById(R.id.validateButton);
        //중복확인 버튼 클릭
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String check_id = editTextId.getText().toString();

                if(validate){
                    return;//검증 완료
                }

                //ID 값을 입력하지 않았다면
                if(check_id.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    dialog = builder.setMessage("아이디를 입력해 주세요.")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }


                //검증시작
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){//json 형식으로 받은 id 값이 success (사용할 수 있는 아이디라면) 다이얼로그 띄움
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                validate = true;//검증완료


                            }else{//json 형식으로 받은 id 값이 success가 아니라면 (사용할 수 없는 아이디라면) 다이얼로그 띄움
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                dialog = builder.setMessage("이미 사용중인 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                //ValidateRequest Java 파일의 함수에 파라미터를 넘겨주고 호출
                ValidateRequest validateRequest = new ValidateRequest(check_id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(validateRequest);
            }
        });


    }

    //회원가입 유효한지 체크하는 함수
    public void insert(View view) {
        String id = editTextId.getText().toString();
        String password = editTextPw.getText().toString();
        String password_check = editTextPw_check.getText().toString();
        String name = editTextName.getText().toString();
        String phone_number = editTextPhone.getText().toString();
        String birth_date = editTextBirth.getText().toString();

        //모든 항목을 입력했는지 체크
        if(id.isEmpty() || password.isEmpty() || password_check.isEmpty() || name.isEmpty() || phone_number.isEmpty() || birth_date.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
            dialog = builder.setMessage("모든 항목을 입력해 주세요.")
                    .setNegativeButton("확인", null)
                    .create();
            dialog.show();
        }
        else
        {
            if(validate)
            {
                //비밀번호와 비밀번호체크가 같은지 확인
                if(password.equals(password_check))
                {
                    insertToDatabase(id, password, name, phone_number, birth_date);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    dialog = builder.setMessage("입력한 비밀번호와 비밀번호확인이 서로 다릅니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                }
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                dialog = builder.setMessage("아이디 중복확인을 해주세요.")
                        .setNegativeButton("확인", null)
                        .create();
                dialog.show();
            }
        }

    }

    //데이터베이스에 정보를 입력하는 함수
    private void insertToDatabase(String id, String password, String name, String phone_number, String birth_date) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "기다려 주세요.", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if(s.equals("회원가입 성공"))
                    finish();

            }
            //백그라운드에서 작업 시작
            @Override
            protected String doInBackground(String... params) {
                try {
                    //파라미터로 각각 변수 지정
                    String id = (String) params[0];
                    String password = (String) params[1];
                    String name = (String) params[2];
                    String phone_number = (String) params[3];
                    String birth_date = (String) params[4];

                    //인코딩하여 php로 전송
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(phone_number, "UTF-8");
                    data += "&" + URLEncoder.encode("birth_date", "UTF-8") + "=" + URLEncoder.encode(birth_date, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    //DB에 전송
                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // 서버에서 Response를 읽어옴
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        //파라미터 넘겨주는 부분
        InsertData task = new InsertData();
        task.execute(id, password, name, phone_number,birth_date);
    }
}