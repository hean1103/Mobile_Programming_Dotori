package com.example.dotori;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {

    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter mAdapter;
    private int complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();

        //mAdapter = new CustomAdapter( mArrayList);
        mAdapter = new CustomAdapter( this, mArrayList);

        mRecyclerView.setAdapter(mAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        /* 1. 편집값을
        AlertDialog.Builder builder2 = new AlertDialog.Builder(ListActivity.this);
        View view2 = LayoutInflater.from(ListActivity.this)
                .inflate(R.layout.edit_box, null, false);
        final CheckBox editCheck = (CheckBox) view2.findViewById(R.id.editcheck_doalog_check);
        boolean strCheck = editCheck.isChecked();
        builder2.setView(view2);
        CheckBox myBox = (CheckBox) findViewById(R.id.check_state);
        myBox.setChecked(strCheck);
        */

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                count++;
//
//                // Dictionary 생성자를 사용하여 ArrayList에 삽입할 데이터를 만듭니다.
//                Dictionary data = new Dictionary(count+"","Apple" + count, "사과" + count);
//
//                //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
//                mArrayList.add(data); // RecyclerView의 마지막 줄에 삽입
//
//                mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                View view = LayoutInflater.from(ListActivity.this)
                        .inflate(R.layout.edit_box, null, false);
                builder.setView(view);
                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                final EditText editTextID = (EditText) view.findViewById(R.id.edittext_dialog_id);
                final EditText editTextList = (EditText) view.findViewById(R.id.edittext_dialog_list);
                final EditText editTextMemo = (EditText) view.findViewById(R.id.edittext_dialog_memo);
                final CheckBox editCheck = (CheckBox) view.findViewById(R.id.editcheck_doalog_check);
                final TextView editText1 = (TextView) view.findViewById(R.id.edittext_dialog_text1);



                ButtonSubmit.setText("삽입");


                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String strID = editTextID.getText().toString();
                        String strList = editTextList.getText().toString();
                        String strMemo = editTextMemo.getText().toString();
                        boolean strCheck = editCheck.isChecked();

                        /* if(strCheck){
                            editText1.setText("완료");
                            complete++;
                        }
                        else
                            editText1.setText("진행중");

                         */

                        String strText1 = editText1.getText().toString();

                        Dictionary dict = new Dictionary(strID, strList, strMemo, strCheck, strText1);

                        //mArrayList.add(0, dict); //첫 줄에 삽입
                        mArrayList.add(dict); //마지막 줄에 삽입
                        mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

}
