package com.example.mapdemo.activity;

import java.util.ArrayList;

import java.util.List;

import android.app.Activity;

import android.os.Bundle;

import android.widget.ArrayAdapter;

import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapdemo.R;
import com.example.mapdemo.model.Contatos;
import com.example.mapdemo.model.EntidadeContatos;

public class ContatosActivity extends AppCompatActivity {

    /** Called when the activity is first created. */

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contatos);

        final ListView listaPessoas = (ListView)findViewById(R.id.ListView01);

        List<EntidadeContatos> ListaContatos = new ArrayList<EntidadeContatos>();

        Contatos Contato = new Contatos(this);

        ListaContatos = Contato.getContatos();

        // adapter que sera o source para a listview

        ArrayAdapter<EntidadeContatos> adapter = new ArrayAdapter<EntidadeContatos>(this,android.R.layout.simple_list_item_1, ListaContatos);

        //seta o adapter para o listview

        listaPessoas.setAdapter(adapter);

    }

}