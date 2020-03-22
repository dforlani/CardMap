package com.example.mapdemo.activity;


import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapdemo.R;
import com.example.mapdemo.model.ContactsResolver;
import com.example.mapdemo.model.EntidadeContato;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteContatosActivity extends AppCompatActivity {
    private List<EntidadeContato> contatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auto_complete_contatos);
        ContactsResolver ctc = new ContactsResolver(this);
       // fillCountryList();
        contatos = ctc.getContatos("");
        AutoCompleteTextView editText = findViewById(R.id.actv);
        AutoCompleteContatosAdapter adapter = new AutoCompleteContatosAdapter(this, contatos);
        editText.setAdapter(adapter);
    }
}