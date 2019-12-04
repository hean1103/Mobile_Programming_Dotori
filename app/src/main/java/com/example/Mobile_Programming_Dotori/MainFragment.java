package com.example.Mobile_Programming_Dotori;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainFragment extends Fragment {

    String id;
    String userImg = "squirrel_";
    String userListNumStr = "2";

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

        GetUserImg task = new GetUserImg();
        try {
            userImg = task.execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_main, container, false);


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


        // 이동거리
//        switch (userListNumStr){
//            case "1":
//                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_anim1);
//                character.startAnimation(anim);
//                break;
//            case "2":
//                Animation anim2 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim2);   // 에니메이션 설정 파일
//                character.startAnimation(anim2);
//                break;
//            case "3":
//                Animation anim3 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim3);   // 에니메이션 설정 파일
//                character.startAnimation(anim3);
//                break;
//            case "4":
//                Animation anim4 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim4);   // 에니메이션 설정 파일
//                character.startAnimation(anim4);
//                break;
//            case "5":
//                Animation anim5 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim5);   // 에니메이션 설정 파일
//                character.startAnimation(anim5);
//                break;
//            case "6":
//                Animation anim6 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim6);   // 에니메이션 설정 파일
//                character.startAnimation(anim6);
//                break;
//            case "7":
//                Animation anim7 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim7);   // 에니메이션 설정 파일
//                character.startAnimation(anim7);
//                break;
//            case "8":
//                Animation anim8 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim8);   // 에니메이션 설정 파일
//                character.startAnimation(anim8);
//                break;
//            case "9":
//                Animation anim9 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim9);   // 에니메이션 설정 파일
//                character.startAnimation(anim9);
//                break;
//        }


        return v;


    }

}
