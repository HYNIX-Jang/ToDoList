package com.hynixlabs.todolist.utils;

/**
 * Created by HYNIX Jang on 2018.04.26
 * For : UserPreference (SharedPreference to save User Infomation)
 * Github : https://github.com/HYNIX-Jang
 */


import android.content.Context;
import android.content.SharedPreferences;

import com.hynixlabs.todolist.vo.UserVO;

public class UserPreference {
    /*Shared Preference*/
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /*저장 변수*/
    public String email;
    public String name;
    public boolean login;

    /*Getter*/
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean isLogin() {
        return login;
    }


    // 생성자
    public UserPreference(Context context) {
        context = context.getApplicationContext(); // 앱의 Context를 가져옴
        preferences = context.getSharedPreferences("User", 0);
        // MODE_PRIVATE
        editor = preferences.edit();
        getUserInfo(); //User정보를 가져옴
    }

    // User의 정보를 Preference에 삽입
    public void saveUserInfo(UserVO vo, boolean isLogin) {
        editor.putString("email", vo.getEmail()); //이메일
        editor.putString("name", vo.getName()); //이름
        editor.putBoolean("isLogin", isLogin); //자동로그인 여부
        editor.apply();
    }


    // User의 정보를 불러옴, KEY | DEFAULT VALUES
    public void getUserInfo() {
        email = preferences.getString("email", null);
        name = preferences.getString("name", null);
        login = preferences.getBoolean("isLogin", false);
    }

    //로그인을 해서 Preference 안에 로그인 정보가 있는지 확인 (이메일 유무로 체크)
    public boolean hasPreference() {
        if (preferences.getString("email", null) == null) { //비어있으면 false
            return false;
        } else return preferences.getString("email", null) != null;
    }

    //유저 확인
    public String[] whoUser() {
        System.out.println("유저는: " + email + " 이고 자동로그인은 " + login + "입니다.");
        return new String[]{email, name};
    }

    //로그아웃 -> 실행할 시 Preference에 있는 User정보가 지워짐
    public void resetPreference() {
        editor.putString("email", null);
        editor.putString("name", null);
        editor.putBoolean("isLogin", false);
        editor.apply();
    }
}

