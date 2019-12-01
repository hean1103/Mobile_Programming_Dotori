
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class StoreFragment extends Fragment {
    ListView listView;
    public String id;
    TextView myPoint;
    Button btn_buy; //구매 버튼
    TextView price; //캐릭터 가격
    TextView textView; //캐릭터 이름
    public String sPoint;

    //메인액티비티로부터 bundle을 사용하여 데이터를 받아옴
    public static StoreFragment newInstance(String param) {
        Bundle args = new Bundle();
        args.putString("userid", param);

        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //로그인 한 유저 아이디를 메인액티비티로부터 받아옴
        if (getArguments() != null) {
            id = getArguments().getString("userid");
            Log.i("프래그먼트 아이디 ", id);
        }

        //사용자의 포인트 가져오기
        GetPoint task = new GetPoint();
        task.execute(id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_store, container, false);
        listView = (ListView)v.findViewById(R.id.listview);
        myPoint = (TextView)v.findViewById(R.id.myPoint);

        //캐릭터 리스트
        ArrayList<String[]> items = new ArrayList<>();
        items.add(new String[] {"cat","20"});
        items.add(new String[] {"dog","35"});
        items.add(new String[] {"ducky","50"});
        items.add(new String[] {"penguin","45"});
        items.add(new String[] {"turtle","30"});


        CustomAdapter adapter = new CustomAdapter(getActivity(), 0, items);
        listView.setAdapter(adapter);


        return v;
    }


    private class CustomAdapter extends ArrayAdapter<String[]> {
        private ArrayList<String[]> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String[]> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.store_itemlist, null);
            }

            // ImageView 인스턴스
            ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
            price = (TextView) v.findViewById(R.id.price);
            textView = (TextView)v.findViewById(R.id.textView);

            // 리스트뷰의 아이템에 이미지를 변경한다.
            if ("cat".equals(items.get(position)[0])){
                imageView.setImageResource(R.drawable.cat);
            }
            else if("dog".equals(items.get(position)[0])) {
                imageView.setImageResource(R.drawable.dog);
            }
            else if("ducky".equals(items.get(position)[0])) {
                imageView.setImageResource(R.drawable.ducky);
            }
            else if("penguin".equals(items.get(position)[0])) {
                imageView.setImageResource(R.drawable.penguin);
            }
            else if("turtle".equals(items.get(position)[0])) {
                imageView.setImageResource(R.drawable.turtle);
            }

            textView.setText(items.get(position)[0]);
            price.setText(items.get(position)[1]);


            btn_buy = (Button)v.findViewById(R.id.buy);

            btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String charPrice = items.get(position)[1];
                    if(sPoint.compareTo(charPrice) == -1) {
                        Toast.makeText(getActivity(),"포인트가 부족합니다 ! 현재 포인트 : "+Integer.parseInt(sPoint) , Toast.LENGTH_SHORT).show();
                    } else {
                        int num = Integer.parseInt(sPoint) - Integer.parseInt(charPrice);
                        BuyItem task = new BuyItem();
                        Toast.makeText(getActivity(),"구매 후 가격 : " + num, Toast.LENGTH_SHORT).show();
                        task.execute(Integer.toString(num), id);
                    }
                }
            });

            return v;
        }
    }

    private class BuyItem extends AsyncTask<String,Void,String> {
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {

            try {
                //인자로 구매하고 난 후의 가격 받아오기
                String price = args[0];
                String id = args[1];

                String urlPath = "http://13.124.77.84/updatePoint.php?price=" + price + "&id=" + id;

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
            myPoint.setText(result);
            sPoint = result;
        }
    }


    private class GetPoint extends AsyncTask<String,Void,String> {
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {

            try {
                //인자로 사용자 아이디 받아오기
                String id = args[0];

                String urlPath = "http://13.124.77.84/getPoint.php?id=" + id;

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
            myPoint.setText(result);
            sPoint = result;
        }
    }

}
