package com.example.mapdemo.model;

import android.graphics.Bitmap;

import java.util.List;

public class Localizacao {
    public Double latitude;
    public Double longitude;
    public Bitmap foto;
    public Integer idContato;

    /**
     * Vai ser obtido na lista de contatos pela idContato
     */
    public String nome;

    /**
     * Vai ser obtido na lista de contatos pela idContato
     */
    public
    List<EntidadeTelefone> telefones;


    public Localizacao(Double latitude, Double longitude, Bitmap foto, Integer  idContato){
        this.latitude = latitude;
        this.longitude = longitude;
        this.foto = foto;
        this.idContato = idContato;
    }

    public String getNome(){
        if(this.nome == null){
            return "";
        }

        return this.nome;
    }

    public List<EntidadeTelefone> getTelefones(){
        if(this.telefones == null){
            return null;

        }

        return this.telefones;
    }


    public String toString(){
        String telefone = "";
        if(this.getTelefones() != null && this.getTelefones().size() > 0){
            telefone = this.getTelefones().get(0).getTelefone();
        }
        return this.getNome() + " : " + telefone;
    }
}

