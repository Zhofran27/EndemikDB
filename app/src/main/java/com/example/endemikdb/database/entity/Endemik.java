package com.example.endemikdb.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "endemik")
public class Endemik {

    @PrimaryKey
    @NonNull
    private String id;
    private String tipe;
    private String nama;
    private String namaLatin;
    private String deskripsi;
    private String asal;
    private String sebaran;
    private String foto;
    private String status;

    public Endemik() {}

    @NonNull public String getId() { return id; }
    public String getTipe() { return tipe; }
    public String getNama() { return nama; }
    public String getNamaLatin() { return namaLatin; }
    public String getDeskripsi() { return deskripsi; }
    public String getAsal() { return asal; }
    public String getSebaran() { return sebaran; }
    public String getFoto() { return foto; }
    public String getStatus() { return status; }

    public void setId(@NonNull String id) { this.id = id; }
    public void setTipe(String tipe) { this.tipe = tipe; }
    public void setNama(String nama) { this.nama = nama; }
    public void setNamaLatin(String namaLatin) { this.namaLatin = namaLatin; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setAsal(String asal) { this.asal = asal; }
    public void setSebaran(String sebaran) { this.sebaran = sebaran; }
    public void setFoto(String foto) { this.foto = foto; }
    public void setStatus(String status) { this.status = status; }
}