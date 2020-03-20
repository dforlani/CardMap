package com.example.mapdemo.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mapdemo.MainActivity;
import com.example.mapdemo.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class DiskPhoneActivity extends AppCompatActivity {
    EditText telefone;
    ImageButton btnLigar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disk_phone);

        telefone = (EditText) findViewById(R.id.phoneNumber);
        btnLigar = (ImageButton) findViewById(R.id.phoneButton);

        btnLigar.setOnClickListener(new View.OnClickListener() {
            //@TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String numero = telefone.getText().toString();
                Uri uri = Uri.parse("tel: " + numero);

                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                if (ActivityCompat.checkSelfPermission(DiskPhoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DiskPhoneActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    return;
                }
                startActivity(intent);
            }
        });

    }

}