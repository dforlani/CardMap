package com.example.mapdemo.model;

import android.graphics.Bitmap;

public class Localizacao {
    public Double latitude;
    public Double longitude;
    public Bitmap foto;

    public Localizacao(Double latitude, Double longitude, Bitmap foto){
        this.latitude = latitude;
        this.longitude = longitude;
        this.foto = foto;    }
}

