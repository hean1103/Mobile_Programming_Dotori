package com.example.Mobile_Programming_Dotori;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class listAdapter extends BaseAdapter {

    private PopupMenu popup; //리스트 뷰에서 옵션 메뉴 기능을 하기 위한 popup
    public String listname;
    public boolean checkState;
    private ArrayList<listItem> listItemList = new ArrayList<listItem>() ; // Adapter에 추가된 데이터를 저장하기 위한 ArrayList

    @Override
    public int getCount() {
        return listItemList.size() ;
    } // Adapter에 사용되는 데이터의 개수를 리턴.


    @Override // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴.
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        // 옵션 메뉴를 위한 이미지 (... 모양)
        final ImageButton iconImageView = (ImageButton) convertView.findViewById(R.id.listview_btn) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView) ;
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.listCheck) ;

        // position을 이용해 정확한 listview의 item을 얻음.
        final listItem listItem = listItemList.get(position);

        iconImageView.setImageDrawable(listItem.getIcon());
        titleTextView.setText(listItem.getTitle());
        checkBox.setChecked(listItem.getCheck());

        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // ... 모양을 클릭했을 경우 (설정 버튼)
                popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.listmenu, popup.getMenu()); // popup메뉴 구성
                popup.show();
                listname = listItemList.get(position).getTitle(); //position을 통해 리스트의 title을 얻음
                checkState = listItemList.get(position).getCheck();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.one: //리스트를 메인에 고정하는 기능
                                Toast.makeText(context,"고정 이벤트 추가하기" ,Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.two: // 리스트의 정보를 수정하는 기능
                                Intent intent = new Intent(context,SettingListActivity.class);
                                intent.putExtra("listname",listname); //settingProjectActovity.java로 리스트 이름 정보를 넘겨줌
                                intent.putExtra("check",checkState);
                                context.startActivity(intent); //액티비티로 화면 전환
                                break;
                            case R.id.three: // 리스트 삭제 기능
                                DeleteToDatabase("hean",listname); // 리스트 삭제를 위하여 ID와 리스트 이름을 매개변수로 설정.
                                break;
                        }
                        return false;
                    }
                });
            }
        });
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴.
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수.
    public void addItem(Drawable icon, String title, String desc, int check) {
        listItem item = new listItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setCheck(check);

        listItemList.add(item);
    }
    //php를 통해 DB와 연동하여 리스트를 삭제하는 함수
    private void DeleteToDatabase(String pid,String pName) {
        class DeleteData extends AsyncTask<String, Void, String> { // 비동기 클래스
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            } // 실행전에 하는 작업
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            } // 실행후에 하는 작업
            @Override
            protected String doInBackground(String... params) {

                String id = (String) params[0]; // 회원의 아이디
                String name = (String) params[1]; // 리스트의 이름
                String data = "pid="+ id +"&listname=" + name; // 2개의 정보를 php에 넘겨줌
                try {
                    // httpURLConnection을 통해 data를 가져온다.
                    URL url = new URL("http://13.124.77.84/deleteProject.php"); // 서버의 php 파일에 연결
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod("POST"); //url 메소드를 post로 결정
                    httpURLConnection.connect();

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(data.getBytes("UTF-8"));
                    outputStream.flush(); // 버퍼링된 출력 바이트 실행
                    outputStream.close();

                    // php문 응답 코드 확인하기
                    int responseStatusCode = httpURLConnection.getResponseCode();
                    Log.i("TAG", "POST response code - " + responseStatusCode);

                    InputStream inputStream;
                    // php문으로부터 온 응답코드가 200일 경우
                    if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    }
                    else{
                        inputStream = httpURLConnection.getErrorStream();
                    }
                    // 요청 결과물 받기
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    // 라인을 받아와 합침
                    while((line = bufferedReader.readLine()) != null){
                        sb.append(line);
                    }


                    bufferedReader.close();
                    return sb.toString();


                } catch (Exception e) { // 에러가 발생한 경우

                    Log.e("ERROR", "InsertData: Error ", e);

                    return new String("Error: " + e.getMessage());
                }
            }
        }
        DeleteData task = new DeleteData();
        task.execute(pid,pName); // 실행
    }
}
