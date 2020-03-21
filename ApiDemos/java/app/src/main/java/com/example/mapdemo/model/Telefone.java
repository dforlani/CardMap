package com.example.mapdemo.model;

import java.util.ArrayList;

import java.util.List;

import android.content.Context;

import android.database.Cursor;

import android.provider.ContactsContract;



public class Telefone {

    private String _IDContato;

    private Context _ctx;

    public Telefone(String IDContato, Context Contexto)

    {

        this._IDContato = IDContato;

        this._ctx = Contexto;

    }



    public List<EntidadeTelefone> getTelefones()

    {

        Cursor C_Telefones =this._ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + _IDContato, null, null);

        int IndexTelefone;

        List<EntidadeTelefone> Telefones = new ArrayList<EntidadeTelefone>();

        while(C_Telefones.moveToNext())

        {

            EntidadeTelefone Telefone = new EntidadeTelefone();

            IndexTelefone = C_Telefones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            Telefone.setTelefone(C_Telefones.getString(IndexTelefone));



            Telefones.add(Telefone);

        }

        C_Telefones.close();

        return Telefones;

    }

}