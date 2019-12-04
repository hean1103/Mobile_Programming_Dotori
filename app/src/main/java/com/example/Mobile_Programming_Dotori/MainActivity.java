package com.example.Mobile_Programming_Dotori;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private ProjectFragment projectFragment = new ProjectFragment();
    private MypageFragment mypageFragment = new MypageFragment();
    private CharacterFragment characterFragment = new CharacterFragment();
    private StoreFragment storeFragment = new StoreFragment();
    private MainFragment mainFragment = new MainFragment();
    public String id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //로그인 한 아이디 값 가져오기
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        Log.i("사용자 아이디 : ", id);

        //storeFragment로 아이디 정보 전송
        Bundle bundle = new Bundle(1);
        bundle.putString("id",id);
        storeFragment.setArguments(bundle);
        characterFragment.setArguments(bundle);
        projectFragment.setArguments(bundle);
        mainFragment.setArguments(bundle);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, projectFragment.newInstance(id)).commitAllowingStateLoss();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_project: {
                        //replace를 사용하면 onDestroyView()가 실행됨.
                        transaction.replace(R.id.frame_layout, projectFragment.newInstance(id)).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_mypage: {
                        transaction.replace(R.id.frame_layout, mypageFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_character: {
                        transaction.replace(R.id.frame_layout, characterFragment.newInstance(id)).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_store: {
                        transaction.replace(R.id.frame_layout, storeFragment.newInstance(id)).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_home: {
                        transaction.replace(R.id.frame_layout, MainFragment.newInstance(id)).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
    }

}