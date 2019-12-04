package com.example.anim;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class GetUserImg extends AsyncTask<String,Void,String> {
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... args) {

        try {
            //인자로 사용자 아이디 받아오기
            String id = args[0];

            String urlPath = "http://13.124.77.84/getUserImg.php?id=" + id;

            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.connect();

            int responseStatusCode = conn.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
                Log.i("http상태 코드 : " , "HTTP_OK");
            }
            else{
                inputStream = conn.getErrorStream();
                Log.i("http상태 코드 : " , "NOT HTTP_OK");
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null){
                Log.i("php 내용 가져오기 : ", line);
                sb.append(line);
            }

            bufferedReader.close();
            Log.i("실행 결과 : ", sb.toString());
            return sb.toString().trim();


        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @Override
    protected void onPostExecute(String result){
    }
}