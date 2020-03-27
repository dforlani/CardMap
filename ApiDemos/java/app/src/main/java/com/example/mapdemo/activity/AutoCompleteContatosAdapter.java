package com.example.mapdemo.activity;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mapdemo.R;
import com.example.mapdemo.model.ContactsResolver;
import com.example.mapdemo.model.EntidadeContato;

import java.util.ArrayList;
import java.util.List;


public class AutoCompleteContatosAdapter extends ArrayAdapter<EntidadeContato> {
    private List<EntidadeContato> contatos;
    List<EntidadeContato> originalValues; // Original Values
    private Context context;


    public AutoCompleteContatosAdapter(@NonNull Context context, @NonNull List<EntidadeContato> contatos) {

        super(context, 0, contatos);
        this.context = context;
        this.contatos = contatos;

        //contatos = new Contatos();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return contatosFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_auto_complete_contatos, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        // ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);

        EntidadeContato contato = getItem(position);

        if (contato != null) {
            textViewName.setText(contato.getNome());
            //imageViewFlag.setImageResource(contato.getFlagImage());
        }

        return convertView;
    }

    private Filter contatosFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // ContactsResolver contatos = new ContactsResolver(context);
            List<EntidadeContato> contatosFiltrado = new ArrayList<>();


            if (originalValues == null) {
                originalValues = new ArrayList<EntidadeContato>(contatos); // saves the original data in mOriginalValues
            }

            //só vai filtrar se tiver digitado ao menos 2 dígitos
            if (constraint == null || constraint.length() == 0) {

                // set the Original result to return
                results.count = originalValues.size();
                results.values = originalValues;
            } else {
                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < originalValues.size(); i++) {
                    EntidadeContato data = originalValues.get(i);
                    if (data.toString().toLowerCase().startsWith(constraint.toString())) {
                        contatosFiltrado.add(data);
                    }
                }
                // set the Filtered result to return
                results.count = contatosFiltrado.size();
                results.values = contatosFiltrado;
            }

            // results.values = listaContatos;
            // results.count = listaContatos.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contatos.clear();
            contatos.addAll((List<EntidadeContato>) results.values); // has the filtered values
            notifyDataSetChanged();  // notifies the data with new filtered values
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((EntidadeContato) resultValue).getNome();
        }
    };
}