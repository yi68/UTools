package com.zym.utools.entity;

import cn.bmob.v3.BmobUser;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.entity
 * 文件名：  User
 * 创建者：  ZYM
 * 时间：   2017/8/2 17:56
 * 描述：   TODO
 */
public class User extends BmobUser {
    private int age;
    private boolean sex;
    private String desc;
    private String icon_login;


    public String getIcon_login() {
        return icon_login;
    }

    public void setIcon_login(String icon_login) {
        this.icon_login = icon_login;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
