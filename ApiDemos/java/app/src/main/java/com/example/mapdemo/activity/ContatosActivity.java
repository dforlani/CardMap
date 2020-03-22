package com.example.mapdemo.activity;

import java.util.ArrayList;

import java.util.List;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapdemo.R;
import com.example.mapdemo.model.ContactsResolver;
import com.example.mapdemo.model.EntidadeContato;

public class ContatosActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    /** Called when the activity is first created. */

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contatos);

        final ListView listaPessoas = (ListView)findViewById(R.id.ListView01);

        List<EntidadeContato> listaContatos = new ArrayList<EntidadeContato>();

        ContactsResolver Contato = new ContactsResolver(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }

        listaContatos = Contato.getContatos("");
        // adapter que sera o source para a listview

        ArrayAdapter<EntidadeContato> adapter = new ArrayAdapter<EntidadeContato>(this,android.R.layout.simple_list_item_1, listaContatos);

        //seta o adapter para o listview

        listaPessoas.setAdapter(adapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}