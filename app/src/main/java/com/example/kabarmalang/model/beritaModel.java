package com.example.kabarmalang.model;

public class beritaModel {
    private int berita_id;
    private String title;
    private String desc;
    private String date;
    private byte[] beritaImage;
    private String location;
    private String latitude;
    private String longitude;

    public beritaModel(int berita_id, String title, String desc, String date, byte[] beritaImage, String location, String latitude, String longitude) {
        this.berita_id = berita_id;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.beritaImage = beritaImage;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
