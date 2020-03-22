package com.example.mapdemo.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.List;
public class ContactsResolver {
    private Context ctx;
    public ContactsResolver(Context contexto)
    {
        this.ctx = contexto;
    }
    public List<EntidadeContato> getContatos(String filter)
    {
        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.STARRED,
                ContactsContract.Contacts.TIMES_CONTACTED,
                ContactsContract.Contacts.LAST_TIME_CONTACTED
        };
        String selection = String.format("%s > 0", ContactsContract.Contacts.HAS_PHONE_NUMBER);
        selection = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
        String[] selectionArgs = {filter+"%"};
//        String sortOrder = String.format(
//                "%s DESC, %s DESC, %S DESC, UPPER(%s) ASC",
//                ContactsContract.Contacts.STARRED,
//                ContactsContract.Contacts.TIMES_CONTACTED,
//                ContactsContract.Contacts.LAST_TIME_CONTACTED,
//                ContactsContract.Contacts.DISPLAY_NAME
//        );
        Cursor cursorContatos
                =this.ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                projection, selection, selectionArgs, ContactsContract.Contacts.DISPLAY_NAME);
        //pega os index das colunnas
        int IndexID = cursorContatos.getColumnIndex(ContactsContract.Contacts._ID);
        int IndexTemTelefone =
                cursorContatos.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

        int IndexName =
                cursorContatos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

        List<EntidadeContato> contatos = new ArrayList<EntidadeContato>();
        EntidadeContato contato;
        while(cursorContatos.moveToNext())
        {
            contato = new EntidadeContato();
            contato.setID(cursorContatos.getString(IndexID));            contato.setNome(cursorContatos.getString(IndexName));
            //verifica se o contato tem telefone
            if(Integer.parseInt(cursorContatos.getString(IndexTemTelefone))>0)
            {
                Telefone _Telefone = new Telefone(contato.getID(), this.ctx);
                contato.setTelefones(_Telefone.getTelefones());
            }
            contatos.add(contato);
        }
        cursorContatos.close();
        return contatos;
    }
}