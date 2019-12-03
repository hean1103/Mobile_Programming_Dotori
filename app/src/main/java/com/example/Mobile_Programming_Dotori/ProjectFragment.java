package com.example.Mobile_Programming_Dotori;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class ProjectFragment extends Fragment {
    public String data[], id;
    ListView listview ;
    listViewAdapter adapter;


    public static ProjectFragment newInstance(String param) {
        Bundle args = new Bundle();
        args.putString("userid", param);

        ProjectFragment fragment = new ProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //로그인 한 유저 아이디를 메인액티비티로부터 받아옴
        if (getArguments() != null) {
            id = getArguments().getString("userid");
        }
        get_data task = new get_data(); // 프로젝트 리스트들을 얻기 위한 객체
        task.execute(id); // 회원 아이디를 변수로 넘겨줌
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        listview = (ListView) view.findViewById(R.id.listview1);
        adapter = new listViewAdapter();
        listview.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_sub1); // 새로운 프로젝트 추가를 위한 + 버튼
        fab.setOnClickListener(new FABClickListener());

        return view ;

    }

    class FABClickListener implements  View.OnClickListener{
        @Override
        // + 버튼을 클릭시 NewProjectActivity.java로 화면 전환
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),NewProjectActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }
    // 프로젝트 리스트들을 얻기 위한 함수
    public class get_data extends AsyncTask<String, Void, String> {  // 비동기 클래스
        protected void onPreExecute() { // 실행전 수행되는 함수
        }

        @Override
        protected String doInBackground (String...params){
        try {
            String id = params[0];
            String uri = "http://13.124.77.84/projectlist.php?PID="+id; // 회원 아이디를 변수로 php에 넘겨줌
            URL url = new URL(uri);
            Log.i("=========================", uri);
            // httpURLConnection을 통해 data를 가져온다.
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET"); //url 메소드를 post로 결정
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
            // 프로젝트 리스트들을 listview 형태로 만들기 위한 선언
            adapter = new listViewAdapter();
            listview.setAdapter(adapter);
            //  json data 분석
            try {
                //php문의 결과를 배열로 받아옴
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                data = new String[jsonArray.length()]; // 배열의 길이만큼 크기 선언

                for (int i = 0; i < jsonArray.length(); i++) {
                    // 배열을 하나씩 객체로 받아 그 안의 PName이라고 저장되있는 값을 data배열에 넣어줌 == 프로젝트 이름
                    jsonObject = jsonArray.getJSONObject(i);
                    data[i] = jsonObject.getString("PName"); // column name
                    // 프로젝트 이름 확인 Log
                    Log.i("php 내용 가져오기 : ", data[i]);
                }

                for(int k = 0 ; k < data.length ; k++) {
                    //data 배열의 크기만큼 for문을 돌며 커스텀 listview 생성
                    adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.menu),
                            data[k], "Account Circle Black 36dp", id) ;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}