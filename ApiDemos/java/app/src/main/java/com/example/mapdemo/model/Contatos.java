package com.example.mapdemo.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.List;
public class Contatos {
    private Context ctx;
    public Contatos(Context contexto)
    {
        this.ctx = contexto;
    }
    public List<EntidadeContatos> getContatos()
    {
        Cursor C_Contatos
                =this.ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        //pega os index das colunnas
        int IndexID = C_Contatos.getColumnIndex(ContactsContract.Contacts._ID);
        int IndexTemTelefone =
                C_Contatos.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

        int IndexName =
                C_Contatos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

        List<EntidadeContatos> Contatos = new ArrayList<EntidadeContatos>();
        EntidadeContatos Contato;
        while(C_Contatos.moveToNext())
        {
            Contato = new EntidadeContatos();
            Contato.setID(C_Contatos.getString(IndexID));            Contato.setNome(C_Contatos.getString(IndexName));
            //verifica se o contato tem telefone
            if(Integer.parseInt(C_Contatos.getString(IndexTemTelefone))>0)
            {
                Telefone _Telefone = new Telefone(Contato.getID(), this.ctx);
                Contato.setTelefones(_Telefone.getTelefones());
            }
            Contatos.add(Contato);
        }
        C_Contatos.close();
        return Contatos;
    }
}