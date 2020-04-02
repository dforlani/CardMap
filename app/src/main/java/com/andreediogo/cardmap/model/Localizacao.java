package com.andreediogo.cardmap.model;

import android.graphics.Bitmap;

import java.util.List;

public class Localizacao {
    public Double latitude;
    public Double longitude;
    public Bitmap foto;
    public Integer idContato;
    public Integer idLocalizacao;

    /**
     * Vai ser obtido na lista de contatos pela idContato
     */
    public String nome;

    /**
     * Vai ser obtido na lista de contatos pela idContato
     */
    public
    List<EntidadeTelefone> telefones;


    /**
     * Vai ser obtido na lista de contatos pela idContato
     */
    public EntidadeContato contato;

    public Localizacao(Double latitude, Double longitude, Bitmap foto, Integer  idContato, Integer idLocalizacao){
        this.latitude = latitude;
        this.longitude = longitude;
        this.foto = foto;
        this.idContato = idContato;
        this.idLocalizacao = idLocalizacao;
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

    public String getTelefonesString(){
        String texto = "";
        if(this.telefones == null){
            for (EntidadeTelefone telefone: telefones
                 ) {
                texto += telefone.getTelefone();
            }

        }
         return texto;
    }

    public String getFirstPhone(){
        String telefone = "";
        if(this.getTelefones() != null && this.getTelefones().size() > 0){
            telefone = this.getTelefones().get(0).getTelefone();
        }
        return  telefone;
    }

    public String toString(){
        String telefone = "";
        if(this.getTelefones() != null && this.getTelefones().size() > 0){
            telefone = this.getTelefones().get(0).getTelefone();
        }
        return this.getNome() + " : " + telefone;
    }
}

