package com.example.mapdemo.model;

import java.util.List;
public class EntidadeContatos {
    private String ID;
    private String Nome;
    private String Email;
    private List<EntidadeTelefone> Telefones;
    public String getID() {
        return ID;
    }
    public void setID(String string) {
        ID = string;
    }
    public String getNome() {
        return Nome;
    }
    public void setNome(String nome) {
        Nome = nome;
    }
    public List<EntidadeTelefone> getTelefones() {
        return Telefones;
    }
    public void setTelefones(List<EntidadeTelefone> telefones) {
        Telefones = telefones;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    //Metodo sobreescrito para que não aparece o nome do componente
    //na listView
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return Nome + "-"  + Telefones.get(0);
    }
}