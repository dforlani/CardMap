package com.example.mapdemo.model;

import java.util.ArrayList;
import java.util.List;
public class EntidadeContatos {
    private String ID;
    private String Nome;
    private String Email;
    private List<EntidadeTelefone> telefones = new ArrayList<>();
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
        return telefones;
    }
    public void setTelefones(List<EntidadeTelefone> telefones) {
        this.telefones = telefones;
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
        String aux_tel = (telefones.size() > 0? telefones.get(0).toString():"");
        return Nome + "-" + ID+ "-" + aux_tel;
    }
}