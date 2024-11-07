package com.blue.qqcommon;

import java.io.Serializable;

/**
 * User: 王帅
 * Date&Time: 2021/11/7 17:27
 * 表示用户信息
 */
public class User implements Serializable {//序列化传输的对象
    private static final long serialVersionUID = 1L;//增强兼容性

    private String userId;//用户名
    private String passwd;//用户密码
    //构造器

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }
    public User(){}
    //getter和setter方法

    public static long getSerialVersionID() {
        return serialVersionUID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
