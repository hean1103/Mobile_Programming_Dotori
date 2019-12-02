package com.example.Mobile_Programming_Dotori;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class MypageFragment extends Fragment {
    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_Password = "password";
    private static final String TAG_Phone = "phone_number";
    private static final String TAG_Birth = "birth_date";
    private static final String TAG_Name = "name";
    private static final String TAG_Point = "point";
    private static final String TAG_Myimage = "myimage";

    TextView vID;
    TextView vPhone;
    TextView vBirth;
    TextView vName;
    TextView vPoint;
    Button Logout;
    ImageView vImage;
    ArrayList<HashMap<String,String>> personList;


    String url = "http://13.124.77.84/Mypage.php";

    JSONArray peoples = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);
        vPhone = (TextView) root.findViewById(R.id.my_phone);
        vBirth = (TextView) root.findViewById(R.id.my_birth);
        vName = (TextView) root.findViewById(R.id.my_name);
        vPoint = (TextView) root.findViewById(R.id.my_point);

        Logout = (Button) root.findViewById(R.id.btn1);
        vImage = (ImageView) root.findViewById(R.id.imageView);
        //공통으로 사용되는 id 변수 불러오기
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        final String g_id = pref.getString("Globalid","");






        personList = new ArrayList<HashMap<String, String>>();

        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params){
                String uri = params[0];

                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json=bufferedReader.readLine())!=null)
                    {
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch(Exception e){
                    return null;
                }
            }


            protected void showList()
            {

                try
                {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    peoples = jsonObj.getJSONArray(TAG_RESULTS);

                    for(int i=0;i<peoples.length();i++)
                    {

                        JSONObject c = peoples.getJSONObject(i);
                        String id = c.getString(TAG_ID);
                        String password = c.getString(TAG_Password);
                        String name = c.getString(TAG_Name);
                        String phone_number = c.getString(TAG_Phone);
                        String birth_date = c.getString(TAG_Birth);
                        String point = c.getString(TAG_Point);
                        String vimg = c.getString(TAG_Myimage);

                        HashMap<String,String> persons = new HashMap<String,String>();

                        if(id.equals(g_id))
                        {
                            vPoint.setText(point);
                            vName.setText(name);
                            vBirth.setText(birth_date);
                            vPhone.setText(phone_number);
                            if(vimg.equals("acron"))
                                vImage.setImageResource(R.drawable.acorn);
                            else if (vimg.equals("cat"))
                                vImage.setImageResource(R.drawable.cat);
                            else if (vimg.equals("ducky"))
                                vImage.setImageResource(R.drawable.ducky);
                            else if (vimg.equals("penguin"))
                                vImage.setImageResource(R.drawable.penguin);
                            else if (vimg.equals("squirrel"))
                                vImage.setImageResource(R.drawable.squirrel);
                            else if (vimg.equals("turtle"))
                                vImage.setImageResource(R.drawable.turtle);
                            else if (vimg.equals("dog"))
                                vImage.setImageResource(R.drawable.dog);
                            else
                                Toast.makeText(getActivity(), "이미지 없음", Toast.LENGTH_LONG).show();

                            break;
                        }

                    }

                } catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            protected void onPostExecute(String result)
            {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        return root;
    }

}