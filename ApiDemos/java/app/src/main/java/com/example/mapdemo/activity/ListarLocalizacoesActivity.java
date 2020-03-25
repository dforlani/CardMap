package com.example.mapdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mapdemo.R;
import com.example.mapdemo.database.DatabaseHelperLocalizacao;
import com.example.mapdemo.model.Localizacao;

import java.util.ArrayList;
import java.util.List;

public class ListarLocalizacoesActivity extends AppCompatActivity {
    List<Localizacao> localizacoes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_localizacoes);

        localizacoes  = this.getLocalizacoes();

        ListView listaDeLocalizacoes = (ListView) findViewById(R.id.lista);
        //ArrayAdapter<Localizacao> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, localizacoes);
        AdapterItemLocalizacao adapter = new AdapterItemLocalizacao(localizacoes, this);
        listaDeLocalizacoes.setAdapter(adapter);

    }

    public List<Localizacao> getLocalizacoes(){
        DatabaseHelperLocalizacao db  = new DatabaseHelperLocalizacao(this);
        ArrayList<Localizacao> localizacoes = db.getAll();

        return localizacoes;

    }
}
