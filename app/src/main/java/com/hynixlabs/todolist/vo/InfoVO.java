package com.hynixlabs.todolist.vo;

/**
 * Created by HYNIX Jang on 2018.04.26
 * For : InfoVO(To save informations through this)
 * Github : https://github.com/HYNIX-Jang
 */


public class InfoVO {
    private int id; //아이디
    private String email, info; //이메일(작성자), 내용
    private boolean likes; //좋아요 확인

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public InfoVO() {
    }

    public InfoVO(int id, String email, String info, boolean likes) {
        this.id = id;
        this.email = email;
        this.info = info;
        this.likes = likes;
    }

    public boolean isLikes() {
        return likes;
    }

    public void setLikes(boolean likes) {
        this.likes = likes;
    }
}
