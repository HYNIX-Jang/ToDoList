package com.hynixlabs.todolist.activity;

/**
 * Created by HYNIX Jang on 2018.04.21
 * For : RegisterActivity
 * Github : https://github.com/HYNIX-Jang
 */

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hynixlabs.todolist.R;
import com.hynixlabs.todolist.vo.UserVO;
import com.hynixlabs.todolist.utils.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    /*DB 설정*/
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    /*Widget*/
    private EditText name, email, password;
    private String nameStr, emailStr, passwordStr;
    private Button btnRegister, btnLogin;

    private UserVO userVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        /*DB 설정*/
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase(); //쓰기가능 DB

        /*FindViewByID*/
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        //Join 버튼 클릭 시
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (register()) { //가입성공하면 로그인 창으로 이동
                    Intent goLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(goLogin);
                    finish();
                }
            }
        });

        //Login 버튼 클릭 시 로그인으로 이동
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goLogin);
                finish();
            }
        });
    }

    //가입
    public boolean register() {
        // toString 및 앞뒤 공백제거, 비밀번호는 공백허용
        nameStr = name.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        passwordStr = password.getText().toString();
        if (isNull()) {
            // 빈칸이 있다면 Toast를 띄움
            Toast.makeText(getApplicationContext(), "모든 항목을 채워주세요.", Toast.LENGTH_SHORT).show();
        } else if ((passwordStr.length() < 6)) {
            // 비밀번호가 6글자보다 적으면 Toast 띄움
            Toast.makeText(getApplicationContext(), "비밀번호를 6글자 이상으로 설정해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            userVO = new UserVO();
            /* Null 확인, 비밀번호 길이 체크 후 실행
            JoinVO에 이름, 이메일, 비밀번호 넣음 */
            userVO.setName(nameStr);
            userVO.setEmail(emailStr);
            userVO.setPassword(passwordStr);

            try { //EXECSQL() EXCEPTION 방지
                database.execSQL("INSERT INTO TB_MEMBERS VALUES(?,?,?)",
                        new String[]{userVO.getName(), userVO.getEmail(), userVO.getPassword()});
                Toast.makeText(getApplicationContext(), "생성했습니다.", Toast.LENGTH_SHORT).show();
                /*insert()함수로 처리할 시 중복 Unique Exception 인식 불가 -> 꼭 execSQL()로 할것*/
                return true;
            } catch (Exception e) { //이메일 중복으로 Unique Exception 발생 -> 중복알림 Toast띄움
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "이미 존재하는 이메일입니다!", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    /*빈칸 + NULL 체크*/
    public boolean isNull() {
        if (nameStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty()) {
            return true;
        }
        return false;
    }

}
