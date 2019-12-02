package com.example.dotori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Dictionary> mList;
    private Context mContext;


    public class CustomViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener { // 1. 리스너 추가
        protected TextView id;
        protected TextView list;
        protected TextView memo;
        protected CheckBox check;
        protected TextView text1;


        public CustomViewHolder(View view) {
            super(view);
            this.id = (TextView) view.findViewById(R.id.id_listitem);
            this.list = (TextView) view.findViewById(R.id.list_listitem);
            this.memo = (TextView) view.findViewById(R.id.memo_listitem);
            this.check = (CheckBox) view.findViewById(R.id.check_state);
            this.text1 = (TextView) view.findViewById(R.id.check_listitem);

            view.setOnCreateContextMenuListener(this); //2. 리스너 등록
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가


            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }

        // 캔텍스트 메뉴 클릭시 동작을 설정
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                switch (item.getItemId()) {
                    case 1001:

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        View view = LayoutInflater.from(mContext)
                                .inflate(R.layout.edit_box, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                        final EditText editTextID = (EditText) view.findViewById(R.id.edittext_dialog_id);
                        final EditText editTextlist = (EditText) view.findViewById(R.id.edittext_dialog_list);
                        final EditText editTextMemo = (EditText) view.findViewById(R.id.edittext_dialog_memo);
                        final CheckBox editCheck = (CheckBox) view.findViewById(R.id.editcheck_doalog_check);
                        final TextView editText1 = (TextView) view.findViewById(R.id.edittext_dialog_text1);



                        editTextID.setText(mList.get(getAdapterPosition()).getId());
                        editTextlist.setText(mList.get(getAdapterPosition()).getList());
                        editTextMemo.setText(mList.get(getAdapterPosition()).getMemo());
                        editCheck.setChecked(mList.get(getAdapterPosition()).getCheck());
                        editText1.setText(mList.get(getAdapterPosition()).getText1());

                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String strID = editTextID.getText().toString();
                                String strlist = editTextlist.getText().toString();
                                String strMemo = editTextMemo.getText().toString();

                                boolean strCheck = editCheck.isChecked();

                                if(strCheck){
                                    editText1.setText("완료");

                                }
                                else
                                    editText1.setText("진행중");

                                String strText1 = editText1.getText().toString();

                                Dictionary dict = new Dictionary(strID, strlist, strMemo, strCheck, strText1);

                                mList.set(getAdapterPosition(), dict);
                                notifyItemChanged(getAdapterPosition());

                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                        break;

                    case 1002:

                        mList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mList.size());

                        break;

                }
                return true;
            }
        };
    }
//    public CustomAdapter(ArrayList<Dictionary> list) {
//        this.mList = list;
//    }

    public CustomAdapter(Context context, ArrayList<Dictionary> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        //final int pos = position;

        viewholder.id.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.list.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.memo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.text1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.id.setGravity(Gravity.CENTER);
        viewholder.list.setGravity(Gravity.CENTER);
        viewholder.memo.setGravity(Gravity.CENTER);
        viewholder.text1.setGravity(Gravity.CENTER);

        viewholder.id.setText(mList.get(position).getId());
        viewholder.list.setText(mList.get(position).getList());
        viewholder.memo.setText(mList.get(position).getMemo());
        viewholder.check.setChecked(mList.get(position).getCheck());
        viewholder.text1.setText(mList.get(position).getText1());

        /*viewholder.check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Dictionary contact = (Dictionary) cb.getTag();

                contact.setSelected(cb.isChecked());
                mList.get(pos).setSelected(cb.isChecked());

                Toast.makeText(
                        v.getContext(),
                        "Clicked on Checkbox: " + cb.getText() + " is "
                                + cb.isChecked(), Toast.LENGTH_LONG).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}