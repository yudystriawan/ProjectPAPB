package com.example.yudystriawan.projectpapb.Data;

public class Review {
    public String nama;
    public String komentar;

    public Review(String nama, String komentar) {
        this.nama = nama;
        this.komentar = komentar;
    }

    public String getNama() {
        return nama;
    }

    public String getKomentar() {
        return komentar;
    }
}
