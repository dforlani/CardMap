package com.example.mapdemo.model;

public class EntidadeTelefone {
    private String Telefone;
    public String getTelefone() {
        return Telefone;

    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Telefone: " + Telefone;
    }
}