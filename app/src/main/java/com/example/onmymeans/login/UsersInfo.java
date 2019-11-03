package com.example.onmymeans.login;


import org.litepal.crud.DataSupport;

public class UsersInfo extends DataSupport {
    private String userName;

    private String passWord;

    public UsersInfo(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName==null?"":userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
