package com.hynixlabs.todolist.adapter;

/**
 * Created by HYNIX Jang on 2018.04.24
 * For : RecyclerAdapter
 * Github : https://github.com/HYNIX-Jang
 */


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hynixlabs.todolist.activity.MainActivity;
import com.hynixlabs.todolist.utils.DBHelper;
import com.hynixlabs.todolist.vo.InfoVO;
import com.hynixlabs.todolist.R;
import com.like.LikeButton;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    /*DB 설정*/
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    private ArrayList<InfoVO> data;
    private Context context;

    //생성자로 데이터 전달받아 넣음
    public RecyclerAdapter(ArrayList<InfoVO> data, Context context) {
        this.data = data;
        this.context = context;

        /*DB 설정*/
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase(); //쓰기가능 DB
    }

    // ViewHolder 생성
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cardview, parent, false);
        return new ViewHolder(view);
    }

    //가변 데이터 처리
    @Override
    @NonNull
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.infoText.setText(data.get(position).getInfo());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, position + "번째 누름", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog(position);
            }
        });

    }

    //데이터 사이즈
    @Override
    public int getItemCount() {
        return data.size();
    }

    //ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView infoText;
        private LikeButton btnLike;
        private CardView cardView;
        private Button btnEdit, btnDelete, btnShare;

        public ViewHolder(View view) {
            super(view);
            infoText = view.findViewById(R.id.titleText);
            btnLike = view.findViewById(R.id.btnLike);
            cardView = view.findViewById(R.id.cardview);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnDelete = view.findViewById(R.id.btnDelete);
            btnShare = view.findViewById(R.id.btnShare);
        }
    }

    public void deleteDialog(final int position) {
        System.out.println("Dialog");
        //        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        //        builder.setTitle("Logout");
        //        builder.setMessage("Are you sure you want to logout?");
        //        builder.setPositiveButton("YES",
        //                new DialogInterface.OnClickListener() {
        //                    public void onClick(DialogInterface dialog, int which) {
        //                        preference.resetPreference();
        //                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        //                        startActivity(intent);
        //                        finish();
        //                    }
        //                });
        //        builder.setNegativeButton("NO", null);
        //        builder.show(); -> Change to another Custom AlertDialog
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure you want to delete?")
                .setConfirmText("YES")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (deleteInfo(data.get(position).getInfo())) { //Delete 성공하면
                            System.out.println("성공해부림");
                            sweetAlertDialog
                                    .setConfirmText("OK")
                                    .setTitleText("Successfully delete it")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        } else {
                            System.out.println("성공안함 ㅡㅡ");
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong!")
                                    .setConfirmText("OK")
                                    .show();
                        }
                    }
                }).show();
    }

    //추가 -> 성공시 true 리턴
    private boolean deleteInfo(String info) {
        try {
            System.out.println("포지션 " + info);
            database.execSQL("DELETE FROM INFO WHERE INFO = ?", //내용, 이메일, 좋아요 체크여부
                    new String[]{info});
            return true; //성공
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
