package com.example.Mobile_Programming_Dotori;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class MainFragment extends Fragment {

    String id;
    String userImg = "squirrel_";
    public String data[] ;
    public int totalNum ,checkNum ;
    public int totalNum1 =10;
    public int  checkNum1 = 3;
    public String PName;

    //메인액티비티로부터 bundle을 사용하여 데이터를 받아옴
    public static MainFragment newInstance(String param) {
        Bundle args = new Bundle();
        args.putString("userid", param);

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getString("userid");
        }
        MainActivity main = new MainActivity();
        PName = main.getProjectName();
        GetUserImg task = new GetUserImg();
        MainFragment.get_data taskData = new MainFragment.get_data(); // 프로젝트 리스트들을 얻기 위한 객체
        taskData.execute(id, PName); // 회원 아이디를 변수로 넘겨줌
        try {
            userImg = task.execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        //캐릭터 지정
        ImageView character = (ImageView) v.findViewById(R.id.imageView1);

        switch (userImg){
            case "cat":
                character.setImageResource(R.drawable.cat);
                break;
            case "ducky":
                character.setImageResource(R.drawable.ducky);
                break;
            case "penguin":
                character.setImageResource(R.drawable.penguin);
                break;
            case "turtle":
                character.setImageResource(R.drawable.turtle);
                break;
            case "squirrel":
                character.setImageResource(R.drawable.squirrel_);
                break;
            case "horse":
                character.setImageResource(R.drawable.horse);
                break;
            case "tiger":
                character.setImageResource(R.drawable.tiger);
                break;
            case "dog":
                character.setImageResource(R.drawable.dog);
                break;
            case "rabbit":
                character.setImageResource(R.drawable.rabbit);
                break;
        }

        // 체크된 리스트 / 전체 리스트 값의 퍼센트에 따라 캐릭터가 이동한다
        // ex) 10퍼센트 이하면 anim1 만큼 이동
        int num = 0;
        num = (checkNum1*100)/totalNum1;

        if(num==0) {
            Animation anim0 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim0);
            character.startAnimation(anim0);
        }
        else if(num<=10) {
            Animation anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim1);
            character.startAnimation(anim1);
        }
        else if(num<=20) {
            Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim2);   // 에니메이션 설정 파일
            character.startAnimation(anim2);
        }
        else if(num<=30) {
            Animation anim3 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim3);   // 에니메이션 설정 파일
            character.startAnimation(anim3);
        }
        else if(num<=40) {
            Animation anim4 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim4);   // 에니메이션 설정 파일
            character.startAnimation(anim4);
        }
        else if(num<=50) {
            Animation anim5 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim5);   // 에니메이션 설정 파일
            character.startAnimation(anim5);
        }
        else if(num<=60) {
            Animation anim6 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim6);   // 에니메이션 설정 파일
            character.startAnimation(anim6);
        }
        else if(num<=80) {
            Animation anim7 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim7);   // 에니메이션 설정 파일
            character.startAnimation(anim7);
        }
        else if(num<=90) {
            Animation anim8 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim8);   // 에니메이션 설정 파일
            character.startAnimation(anim8);
        }
        else if(num<=100) {
                Animation anim9 = AnimationUtils.loadAnimation (getActivity(), R.anim.translate_anim9);   // 에니메이션 설정 파일
                character.startAnimation(anim9);
        }

        return v;
    }
    public class get_data extends AsyncTask<String, Void, String> {  // 비동기 클래스
        protected void onPreExecute() { // 실행전 수행되는 함수
        }

        @Override
        protected String doInBackground (String...params){
            try {
                String id = params[0];
                String pname = params[1];
                String uri = "http://13.124.77.84/getlistnum.php?PID="+id+"&PName=" +pname; // 회원 아이디를 변수로 php에 넘겨줌
                URL url = new URL(uri);
                Log.i("=========================", uri);
                // httpURLConnection을 통해 data를 가져온다.
                HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setReadTimeout(5000);
                httpsURLConnection.setConnectTimeout(5000);
                httpsURLConnection.connect();
                // php문 응답 코드 확인하기
                int responseStatusCode = httpsURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                // php문으로부터 온 응답코드가 200일 경우
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpsURLConnection.getInputStream();
                    Log.i("http상태 코드 : " , "HTTP_OK");
                }
                else{
                    inputStream = httpsURLConnection.getErrorStream();
                    Log.i("http상태 코드 : " , "NOT HTTP_OK");
                }
                // 요청 결과물 받기
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                //php문 결과 내용 가져오기 라인으로 받아오기
                while((line = bufferedReader.readLine()) != null){
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
                Log.e("log_tag1", "Error converting result "+e.toString());
                return e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String result){
            //  json data 분석
            try {
                //php문의 결과를 배열로 받아옴
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                data = new String[jsonArray.length()]; // 배열의 길이만큼 크기 선언

                for (int i = 0; i < jsonArray.length(); i++) {
                    // 배열을 하나씩 객체로 받아 그 안의 PName이라고 저장되있는 값을 data배열에 넣어줌 == 프로젝트 이름
                    jsonObject = jsonArray.getJSONObject(i);
                    data[i] = jsonObject.getString("CheckBox"); // column name
                    // 프로젝트 이름 확인 Log
                    Log.i("php 내용 가져오기 : ", data[i]);
                }
                totalNum = data.length;
                for (int k = 0 ; k < data.length ; k++){
                    if(data[k].equals("1")) {
                        checkNum += 1;
                    }
                }
                Log.i("체크 갯수 ", checkNum + "==!");
                Log.i("전체 갯수 ", totalNum + "==!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
