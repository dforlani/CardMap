package com.example.mapdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;
import java.util.List;

import com.example.mapdemo.R;
import com.example.mapdemo.database.DatabaseHelperLocalizacao;
import com.example.mapdemo.model.Localizacao;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterItemLocalizacao extends BaseAdapter implements View.OnClickListener {

    List<Localizacao> localizacoes;
    private final Activity act;

    public AdapterItemLocalizacao(List<Localizacao> localizacoes, Activity act) {
        this.localizacoes = localizacoes;
        this.act = act;
    }

    @Override public int getCount() {
        if(localizacoes != null)
            return localizacoes.size();
        else return 0;
    }


    @Override public Object getItem(int position) {
        return localizacoes.get(position);
    }


    @Override
    public long getItemId(int position) {
        return localizacoes.get(position).idLocalizacao;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.adapter_item_localizacao, parent, false);
        Localizacao localizacao = localizacoes.get(position);

        //pegando as referências das Views
        TextView nome = (TextView) view.findViewById(R.id.lista_nome);
        TextView telefone = (TextView) view.findViewById(R.id.lista_telefone);
        ImageView imagem = (ImageView) view.findViewById(R.id.lista_imagem);
        Button onRemover = view.findViewById((R.id.bttRemover));
        onRemover.setOnClickListener(new ClickListenerRemover(act.getBaseContext(), localizacao));

        //populando as Views
        nome.setText(localizacao.getNome());
        telefone.setText(localizacao.getTelefonesString());
        imagem.setImageBitmap(localizacao.foto);
        return view;
    }

    /**
     * Remover o usuário ao clicar
     * */
    public void onClick(View view) {
        Toast.makeText(act.getBaseContext(), "Cliquei", Toast.LENGTH_SHORT).show();
    }

    public  class ClickListenerRemover extends View implements View.OnClickListener
    {
        Localizacao localizacao;
        public ClickListenerRemover(Context context, Localizacao localizacao)
        {
            super(context);
            this.localizacao = localizacao;
            this.setOnClickListener(this);
        }

        //protected abstract void setOnClickListerner(ChordDiagram chordDiagram);

        @Override
        public void onClick(View v)
        {
            DatabaseHelperLocalizacao database = new DatabaseHelperLocalizacao(this.getContext());
            database.remover(localizacao);

            Toast.makeText(act.getBaseContext(), "Item removido com sucesso", Toast.LENGTH_SHORT).show();
            act.finish();


        }
    }
}
