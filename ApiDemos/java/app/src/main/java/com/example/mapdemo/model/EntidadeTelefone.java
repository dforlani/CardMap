package com.example.mapdemo.model;

public class EntidadeTelefone {
    private String telefone;
    public String getTelefone() {
        return telefone;

    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Telefone: " + telefone;
    }
}