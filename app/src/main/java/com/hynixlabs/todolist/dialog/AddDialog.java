package com.hynixlabs.todolist.dialog;

/**
 * Created by HYNIX Jang on 2018.04.26
 * For : AddDialog
 * Github : https://github.com/HYNIX-Jang
 */


import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hynixlabs.todolist.R;
import com.hynixlabs.todolist.utils.DBHelper;
import com.hynixlabs.todolist.utils.UserPreference;

public class AddDialog extends Dialog {

    /*Preference*/
    private UserPreference preference;

    /*DB 설정*/
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Cursor cursor;

    private EditText editText; //입력창
    private Button btnAdd; //추가버튼
    private String text; //toString()

    private String email;
    private Context context;


    public AddDialog(@NonNull Context context) {
        super(context);
        this.context = context; //생성자로 Context 받아와서 저장
    }

    public AddDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AddDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dialog);

        /*DB 설정*/
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase(); //쓰기가능 DB

        /*Preference*/
        preference = new UserPreference(context);
        email = preference.getEmail();

        /*FindViewByID*/
        editText = findViewById(R.id.type);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = editText.getText().toString().trim(); //toString() + 공백제거
                System.out.println("삽입 될 내용: " + text);
                if (!isNull()) { //내용이 비어있지 않고 중복이 아닐 때
                    if (insertInfo()) { //추가 성공했을 때
                        Toast.makeText(context, "추가되었습니다!", Toast.LENGTH_SHORT).show();
                        System.out.println("추가되었습니다!");
                        dismiss(); //Dialog 닫음
                    }
                } else { //!isNull() -> 입력칸이 빈칸일 때
                    Toast.makeText(context, "입력값이 빈칸이에요!", Toast.LENGTH_SHORT).show();
                    System.out.println("추가하지 못했습니다ㅜㅡㅜ");
                }
            }
        });
    }

    //입력창이 비어있는지 확인
    private boolean isNull() {
        return text.isEmpty();
    }

    //추가 -> 성공시 true 리턴
    private boolean insertInfo() {
        try {
            System.out.println("이메일은 " + preference.email + "입니다.");
            /*내용 / 이메일 / 좋아요 여부 순 */
            database.execSQL("INSERT INTO INFO (INFO, EMAIL, LIKES) VALUES(?,?,?)", //내용, 이메일, 좋아요 체크여부
                    new String[]{text, email, "false"});
            return true; //성공
        } catch (Exception e) {
            // 내용 중복 불허
            e.printStackTrace();
            Toast.makeText(context, "값이 중복되면 안돼요 ㅜㅡㅜ", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
