package com.hynixlabs.todolist.activity;

/**
 * Created by HYNIX Jang on 2018.04.21
 * For : LoginActivity
 * Github : https://github.com/HYNIX-Jang
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hynixlabs.todolist.R;
import com.hynixlabs.todolist.vo.UserVO;
import com.hynixlabs.todolist.utils.DBHelper;
import com.hynixlabs.todolist.utils.UserPreference;

public class LoginActivity extends AppCompatActivity {

    /*DB 설정*/
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    /*Preference*/
    private UserPreference preference;

    /*Widget*/
    private EditText email, password;
    private Button btnRegister, btnLogin;
    private CheckBox btnAutoLogin;
    private UserVO vo;

    /*toString() 저장*/
    private String emailStr, passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*DB 설정*/
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase(); //쓰기가능 DB

        /*FindViewByID*/
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnAutoLogin = findViewById(R.id.btnAutoLogin);

        /*Preference*/
        preference = new UserPreference(this);

        //가입버튼 누를 시
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //로그인버튼 누를 시
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //계정이 존재하고, 아이디와 비밀번호가 매칭될 시
                if (checkAccount()) {
                    Toast.makeText(getApplicationContext(), "로그인 되었습니다!", Toast.LENGTH_LONG).show();
                    //메인 기능으로 이동하는 Intent 생성
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); //로그인창 종료
                } else { //비밀번호가 틀렸거나, 존재하지 않는 계정일 경우(!checkAccount())
                    Toast.makeText(getApplicationContext(), "등록되지 않은 아이디이거나" + "\n" +
                            "아이디 또는 비밀번호를 잘못 입력하셨습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //아이디(이메일)의 존재, 아이디와 비밀번호 매칭 확인
    public boolean checkAccount() {
        String sql;
        emailStr = email.getText().toString().trim();
        passwordStr = password.getText().toString();
        sql = "SELECT NAME, EMAIL, PASSWORD FROM TB_MEMBERS WHERE EMAIL = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{emailStr}); //이메일로 검색

        //아이디가 있을 때 실행, 아이디가 없으면 바로 false 리턴
        if (cursor.getCount() == 1) {
            while (cursor.moveToNext()) {
                vo = new UserVO();
                vo.setName(cursor.getString(0));
                vo.setEmail(cursor.getString(1));
                vo.setPassword(cursor.getString(2));
            }

            //SELECT한 비밀번호와 입력한 비밀번호가 일치하면 Preference에 정보저장
            if (vo.getPassword().equals(passwordStr)) {
                preference.saveUserInfo(vo, isAutoLogin()); //saveUserInfo(계정정보, 자동로그인 여부)
                return true;
            }
        }

        return false; //아이디가 없거나 비밀번호 틀리면 false 리턴 -> Toast띄움
    }

    //자동 로그인 허용여부
    public boolean isAutoLogin() {
        if (btnAutoLogin.isChecked()) {
            System.out.println("Checked");
            return true; //허용
        }
        System.out.println("No Checked");
        return false; //불허
    }

}
