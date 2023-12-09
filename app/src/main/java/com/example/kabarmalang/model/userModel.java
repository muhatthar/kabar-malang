package com.example.kabarmalang.model;

public class userModel {
    private String nama;
    private String key;

    public userModel() {

    }

    public userModel(String nama) {
        this.nama = nama;
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
