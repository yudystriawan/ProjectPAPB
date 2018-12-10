package com.example.yudystriawan.projectpapb.Data;

public class Review {
    public String nama;
    public String komentar;
    public String id;
    public String db;
    public String indeks;

    public Review(String nama, String komentar, String id, String db, String indeks) {
        this.nama = nama;
        this.komentar = komentar;
        this.id = id;
        this.db = db;
        this.indeks = indeks;
    }

    public String getNama() {
        return nama;
    }

    public String getKomentar() {
        return komentar;
    }

    public String getId() {
        return id;
    }

    public String getDb() {
        return db;
    }

    public String getIndeks() {
        return indeks;
    }
}
