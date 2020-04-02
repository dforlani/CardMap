package com.andreediogo.cardmap.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.andreediogo.cardmap.R;
import com.andreediogo.cardmap.database.DatabaseHelperLocalizacao;
import com.andreediogo.cardmap.model.Localizacao;

import java.util.List;

public class AdapterItemLocalizacao extends BaseAdapter {

    List<Localizacao> localizacoes;
    private final Activity act;

    public AdapterItemLocalizacao(List<Localizacao> localizacoes, Activity act) {
        this.localizacoes = localizacoes;
        this.act = act;
    }

    @Override
    public int getCount() {
        if (localizacoes != null)
            return localizacoes.size();
        else return 0;
    }


    @Override
    public Object getItem(int position) {
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

        Button bttRemover = view.findViewById((R.id.bttRemover));
        bttRemover.setOnClickListener(new ClickListenerRemover(act.getBaseContext(), localizacao));

        Button bttEditar = view.findViewById((R.id.bttEditar));
        bttEditar.setOnClickListener(new ClickListenerEditar(act.getBaseContext(), localizacao));

        Button bttDiscar = view.findViewById((R.id.bttDiscar));
        bttDiscar.setOnClickListener(new ClickListenerDiscar(act.getBaseContext(), localizacao));

        //populando as Views
        nome.setText(localizacao.getNome());
        telefone.setText(localizacao.getTelefonesString());
        imagem.setImageBitmap(localizacao.foto);
        return view;
    }


    public class ClickListenerRemover extends View implements View.OnClickListener {
        Localizacao localizacao;

        public ClickListenerRemover(Context context, Localizacao localizacao) {
            super(context);
            this.localizacao = localizacao;
            this.setOnClickListener(this);
        }

        //protected abstract void setOnClickListerner(ChordDiagram chordDiagram);

        @Override
        public void onClick(View v) {
            DatabaseHelperLocalizacao database = new DatabaseHelperLocalizacao(this.getContext());
            database.remover(localizacao);

            Toast.makeText(act.getBaseContext(), "Item removido com sucesso", Toast.LENGTH_SHORT).show();
            act.finish();


        }
    }

    public class ClickListenerEditar extends View implements View.OnClickListener {
        Localizacao localizacao;

        public ClickListenerEditar(Context context, Localizacao localizacao) {
            super(context);
            this.localizacao = localizacao;
            this.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(act.getBaseContext(), NovaLocalizacaoComFotoActivity.class);
            intent.putExtra("ID_LOCALIZACAO", localizacao.idLocalizacao.toString());
            act.startActivity(intent);
        }
    }

    public class ClickListenerDiscar extends View implements View.OnClickListener {
        Localizacao localizacao;

        public ClickListenerDiscar(Context context, Localizacao localizacao) {
            super(context);
            this.localizacao = localizacao;
            this.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("tel: " + localizacao.getFirstPhone());
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            }
            act.startActivity(intent);
        }
    }
}
