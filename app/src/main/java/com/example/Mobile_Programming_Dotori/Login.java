package com.example.Mobile_Programming_Dotori;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText) findViewById(R.id.IDView);
        final EditText pwText = (EditText) findViewById(R.id.PWView);
        final Button LoginButton = (Button) findViewById(R.id.LoginButton);
        final Button RegisterButton = (Button) findViewById(R.id.RegisterButton);


        //로그인클릭


        LoginButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Toast.makeText(getApplicationContext(),"로그인 성공!", Toast.LENGTH_SHORT).show();
                                                  Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                  startActivity(intent);
                                              }
                                          }
        );

//        LoginButton.setOnClickListener(new View.OnClickListener() {
//                                           @Override
//                                           public void onClick(View v) {
//                                               final String id = idText.getText().toString();
//                                               final String password = pwText.getText().toString();
//
//                                               Response.Listener<String> responseListener = new Response.Listener<String>() {
//                                                   @Override
//                                                   public void onResponse(String response) {
//                                                       try{
//                                                           JSONObject jsonResponse = new JSONObject(response);
//                                                           boolean success = jsonResponse.getBoolean("success");
//                                                           if(success)
//                                                           {
//                                                               String id = jsonResponse.getString("id");
//                                                               String password = jsonResponse.getString("password");
//
//                                                               Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                                                               Toast.makeText(getApplicationContext(),"로그인 성공!", Toast.LENGTH_SHORT).show();
//                                                               startActivity(intent);
//                                                           }
//                                                           else
//                                                           {
//                                                               AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
//                                                               builder.setMessage("회원정보가 맞지 않습니다.")
//                                                                       .setNegativeButton("다시 시도해 주세요.",null)
//                                                                       .create()
//                                                                       .show();
//                                                           }
//                                                       }catch (Exception e)
//                                                       {
//                                                           e.printStackTrace();
//                                                       }
//                                                   }
//                                               };
//                                               LoginRequest loginRequest = new LoginRequest(id, password, responseListener);
//                                               RequestQueue queue = Volley.newRequestQueue(Login.this);
//                                               queue.add(loginRequest);
//                                           }
//                                       }
//        );

        //회원가입 클릭

        RegisterButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Toast.makeText(getApplicationContext(),"회원가입 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                                  Intent intent2 = new Intent(getApplicationContext(),Register.class);
                                                  startActivity(intent2);
                                              }
                                          }
        );
    }

}
