package com.example.kabarmalang.model;

public class userModel {
    private String nama;
    private String key;

    public userModel(String nama, String key) {
        this.nama = nama;
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
