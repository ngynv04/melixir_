package com.example.melixir.model;

import java.io.Serializable;

public class User implements Serializable {
    private Integer IDCus;
    private String NameCus;
    private String PhoneCus;
    private String EmailCus;
    private String PassCus;

    public User() {
    }

    public User(Integer IDCus, String nameCus, String phoneCus, String emailCus, String passCus) {
        this.IDCus = IDCus;
        NameCus = nameCus;
        PhoneCus = phoneCus;
        EmailCus = emailCus;
        PassCus = passCus;
    }

    public Integer getIDCus() {
        return IDCus;
    }

    public void setIDCus(Integer IDCus) {
        this.IDCus = IDCus;
    }

    public String getNameCus() {
        return NameCus;
    }

    public void setNameCus(String nameCus) {
        NameCus = nameCus;
    }

    public String getPhoneCus() {
        return PhoneCus;
    }

    public void setPhoneCus(String phoneCus) {
        PhoneCus = phoneCus;
    }

    public String getEmailCus() {
        return EmailCus;
    }

    public void setEmailCus(String emailCus) {
        EmailCus = emailCus;
    }

    public String getPassCus() {
        return PassCus;
    }

    public void setPassCus(String passCus) {
        PassCus = passCus;
    }

    @Override
    public String toString() {
        return IDCus +"\t"+NameCus+"\t"+EmailCus+"\t"+PassCus+"\t"+PhoneCus;

    }
}
