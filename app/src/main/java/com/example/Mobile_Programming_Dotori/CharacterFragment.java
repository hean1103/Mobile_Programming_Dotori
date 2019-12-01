
package com.example.Mobile_Programming_Dotori;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CharacterFragment extends Fragment {
    ListView listView;
    public String id;
    Button btn_select; //선택 버튼
    TextView textView; //캐릭터 이름
    public String myChar; //캐릭터


    public static CharacterFragment newInstance(String param) {
        Bundle args = new Bundle();
        args.putString("userid", param);

        CharacterFragment fragment = new CharacterFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GetImage task = new GetImage();
        try {
            myChar = task.execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_character, container, false);
        listView = (ListView)v.findViewById(R.id.listview);

        //캐릭터 리스트
        ArrayList<String> items = new ArrayList<>();
        String str[] = myChar.split(",");
        Collections.addAll(items, str);

        CustomAdapter adapter = new CustomAdapter(getActivity(), 0, items);
        listView.setAdapter(adapter);

        return v;
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.listview_character, null);
            }

            // ImageView 인스턴스
            ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
            textView = (TextView)v.findViewById(R.id.textView);

            // 리스트뷰의 아이템에 이미지를 변경한다.
            if ("cat".equals(items.get(position))){
                imageView.setImageResource(R.drawable.cat);
            }
            else if("dog".equals(items.get(position))) {
                imageView.setImageResource(R.drawable.dog);
            }
            else if("ducky".equals(items.get(position))) {
                imageView.setImageResource(R.drawable.ducky);
            }
            else if("penguin".equals(items.get(position))) {
                imageView.setImageResource(R.drawable.penguin);
            }
            else if("turtle".equals(items.get(position))) {
                imageView.setImageResource(R.drawable.turtle);
            }

            textView.setText(items.get(position));

            btn_select = (Button)v.findViewById(R.id.btn_select);

            btn_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateImage task = new updateImage();
                    task.execute( id, items.get(position));
                    Toast.makeText(getActivity(),"대표 이미지로 저장되었습니다.", Toast.LENGTH_SHORT).show();

                }
            });

            return v;
        }
    }


    private class GetImage extends AsyncTask<String,Void,String>{
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {

            try {
                //인자로 사용자 아이디 받아오기
                String id = args[0];

                String urlPath = "http://13.124.77.84/getImage.php?id=" + id;

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

    private class updateImage extends AsyncTask<String,Void,String> {
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {

            try {
                //인자로 구매하고 난 후의 가격 받아오기
                String id = args[0];
                String name = args[1]; //업데이트할 사용자의 아이디

                String urlPath = "http://13.124.77.84/updateImage.php?id=" + id + "&myImage=" + name ;

                URL url = new URL(urlPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.connect();

                int responseStatusCode = conn.getResponseCode();
                Log.d(TAG, "response code : " + responseStatusCode);

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
}
