package com.example.Mobile_Programming_Dotori;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class GetUserListNumStr extends AsyncTask<String,Void,String> {
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String id = params[0];
            String PName = params[1];
            String uri = "http://13.124.77.84/getprojectlist.php?PID=" + id + "&PName=" + PName; // 회원 아이디와 프로젝트이름을 변수로 php에 넘겨줌 디비 php 경로
            URL url = new URL(uri);
            // httpURLConnection을 통해 data를 가져온다.
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET"); //url 메소드
            httpsURLConnection.setReadTimeout(5000);
            httpsURLConnection.setConnectTimeout(5000);
            httpsURLConnection.connect();
            // php문 응답 코드 확인하기
            int responseStatusCode = httpsURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            // php문으로부터 온 응답코드가 200일 경우
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpsURLConnection.getInputStream();
                Log.i("http상태 코드 : ", "HTTP_OK");
            } else {
                inputStream = httpsURLConnection.getErrorStream();
                Log.i("http상태 코드 : ", "NOT HTTP_OK");
            }
            // 요청 결과물 받기
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            //php문 결과 내용 가져오기 라인으로 받아오기
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            // 실행 결과 확인 Log
            Log.i("실행 결과 : ", sb.toString());
            // onPostExecute로 결과값 전달
            return sb.toString().trim();

        } catch (MalformedURLException e) {
            return e.getMessage();
        } catch (IOException e) {
            Log.e("log_tag1", "Error converting result " + e.toString());
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
    }
}
