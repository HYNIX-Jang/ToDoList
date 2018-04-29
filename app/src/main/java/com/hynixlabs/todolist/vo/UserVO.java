package com.hynixlabs.todolist.vo;

/**
 * Created by HYNIX Jang on 2018.04.24
 * For : UserVO(To save Users Informations)
 * Github : https://github.com/HYNIX-Jang
 */

public class UserVO {
    private String name, email, password; //이름, 이메일, 비밀번호

    public UserVO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserVO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

