package com.example.kabarmalang.model;

public class beritaModel {
    private int berita_id;
    private String title;
    private String desc;
    private String date;
    private byte[] beritaImage;

    public beritaModel(int berita_id, String title, String desc, String date, byte[] beritaImage) {
        this.berita_id = berita_id;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.beritaImage = beritaImage;
    }

    public int getBerita_id() {
        return berita_id;
    }

    public void setBerita_id(int berita_id) {
        this.berita_id = berita_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getBeritaImage() {
        return beritaImage;
    }

    public void setBeritaImage(byte[] beritaImage) {
        this.beritaImage = beritaImage;
    }
}
