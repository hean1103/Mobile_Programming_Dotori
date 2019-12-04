package com.example.anim;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainFragment extends AppCompatActivity {

    String id;
    String PName;

    String userImg = "squirrel_";
    String userListNumStr = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetUserImg task = new GetUserImg();
        try {
            userImg = task.execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

         */
        /*GetUserListNumStr task2 = new GetUserListNumStr();
        try {
            userListNumStr = task2.execute(id, PName).get();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //캐릭터 지정
        ImageView character = (ImageView)findViewById(R.id.imageView1);

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
        }

        // 이동거리
        switch (userListNumStr){
            case "1":
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_anim1);
                character.startAnimation(anim);
                break;

            case "2":
                Animation anim2 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim2);   // 에니메이션 설정 파일
                character.startAnimation(anim2);
                break;
            case "3":
                Animation anim3 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim3);   // 에니메이션 설정 파일
                character.startAnimation(anim3);
                break;
            case "4":
                Animation anim4 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim4);   // 에니메이션 설정 파일
                character.startAnimation(anim4);
                break;
            case "5":
                Animation anim5 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim5);   // 에니메이션 설정 파일
                character.startAnimation(anim5);
                break;
            case "6":
                Animation anim6 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim6);   // 에니메이션 설정 파일
                character.startAnimation(anim6);
                break;
            case "7":
                Animation anim7 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim7);   // 에니메이션 설정 파일
                character.startAnimation(anim7);
                break;
            case "8":
                Animation anim8 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim8);   // 에니메이션 설정 파일
                character.startAnimation(anim8);
                break;
            case "9":
                Animation anim9 = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.translate_anim9);   // 에니메이션 설정 파일
                character.startAnimation(anim9);
                break;
        }

    }

}
