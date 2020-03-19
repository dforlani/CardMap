package com.example.mapdemo.model;

import android.graphics.Bitmap;

public class Localizacao {
    public Double latitude;
    public Double longitude;
    public Bitmap foto;
    public String nome;
    public String telefone;

    public Localizacao(Double latitude, Double longitude, Bitmap foto, String nome, String telefone){
        this.latitude = latitude;
        this.longitude = longitude;
        this.foto = foto;
        this.nome = nome;
        this.telefone = telefone;
    }
}

