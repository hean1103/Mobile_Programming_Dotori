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
        }

        //사용자의 포인트 가져오기
        GetPoint task = new GetPoint();
        try {
            sPoint = task.execute(id).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_store, container, false);
        listView = (ListView)v.findViewById(R.id.listview);
        myPoint = (TextView)v.findViewById(R.id.myPoint);
        myPoint.setText(sPoint); //데이터베이스로부터 받아온 사용자의 포인트를 textView에 넣어줌

        //캐릭터 리스트 생성 (캐릭터 이름, 가격)
        ArrayList<String[]> items = new ArrayList<>();
        items.add(new String[] {"cat","20"});
        items.add(new String[] {"dog","35"});
        items.add(new String[] {"ducky","10"});
        items.add(new String[] {"penguin","45"});
        items.add(new String[] {"turtle","30"});
        items.add(new String[] {"rabbit","20"});
        items.add(new String[] {"tiger","50"});
        items.add(new String[] {"horse","75"});

        CustomAdapter adapter = new CustomAdapter(getActivity(), 0, items);
        listView.setAdapter(adapter);

        return v;
    }


    private class CustomAdapter extends ArrayAdapter<String[]> {
        //캐릭터 목록 리스트
        private ArrayList<String[]> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String[]> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        //store_itemlist에 있는 레아아웃에 넣어줌
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

            // 리스트뷰의 아이템에 이미지를 변경함 (캐릭터 이름에 해당하는 이미지를 drawable폴더에서 가져옴)
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
            else if("rabbit".equals(items.get(position)[0])) {
                imageView.setImageResource(R.drawable.rabbit);
            }
            else if("tiger".equals(items.get(position)[0])) {
                imageView.setImageResource(R.drawable.tiger);
            }
            else if("horse".equals(items.get(position)[0])) {
                imageView.setImageResource(R.drawable.horse);
            }

            textView.setText(items.get(position)[0]); //캐릭터 이름
            price.setText(items.get(position)[1]); //캐릭터 가격

            btn_buy = (Button)v.findViewById(R.id.buy);

            btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String charPrice = items.get(position)[1];
                    int num1 = Integer.parseInt(sPoint); //현재 가지고 있는 포인트
                    int num2 = Integer.parseInt(charPrice); //사고자 하는 캐릭터의 포인트
                    int num = num1 - num2;
                    //포인트가 없거나 구매하기에 부족한 경우
                    if(num <= 0) {
                        Toast.makeText(getActivity(),"포인트가 부족합니다 ! 현재 포인트 : "+Integer.parseInt(sPoint) , Toast.LENGTH_SHORT).show();
                    } else {
                        //구매가 가능한 경우에는 데이터베이스의 사용자 포인트를 구매 후의 가격으로 업데이트함
                        BuyItem task = new BuyItem();
                        Toast.makeText(getActivity(),"구매 후 가격 : " + num, Toast.LENGTH_SHORT).show();
                        task.execute(Integer.toString(num), id, items.get(position)[0]);
                    }
                }
            });

            return v;
        }
    }

    //데이터베이스에서 캐릭터 구매 후 사용자의 포인트를 업데이트하고, 사용자의 캐릭터 목록에 새로운 캐릭터 이름을 추가하는 클래스
    private class BuyItem extends AsyncTask<String,Void,String> {
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {

            try {
                String price = args[0]; //구매하고 난 후의 가격
                String id = args[1]; //사용자의 아이디
                String myImage = args[2]; //구매한 캐릭터이름

                //연결할 php파일 경로
                String urlPath = "http://13.124.77.84/updatePoint.php?price=" + price + "&id=" + id + "&myImage=" + myImage;
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


    // 데이터베이스에서 사용자의 현재 가지고 있는 포인트를 가져오는 클래스
    private class GetPoint extends AsyncTask<String,Void,String> {
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {

            try {
                //인자로 사용자 아이디 받아오기
                String id = args[0];

                // 연결할 php파일 경로
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
        }
    }

}


