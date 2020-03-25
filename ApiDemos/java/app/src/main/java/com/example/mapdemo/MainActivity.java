/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mapdemo.activity.ListarLocalizacoesActivity;
import com.example.mapdemo.activity.NovaLocalizacaoComFotoActivity;
import com.example.mapdemo.database.DatabaseHelperLocalizacao;
import com.example.mapdemo.model.Localizacao;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * This shows how to place markers on a map.
 */
public class MainActivity extends AppCompatActivity implements
        OnMarkerClickListener,
        OnInfoWindowClickListener,
        OnMarkerDragListener,
        OnSeekBarChangeListener,
        OnInfoWindowLongClickListener,
        OnInfoWindowCloseListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {


    private GoogleMap mMap;

    private final List<Marker> mMarkerRainbow = new ArrayList<>();


    List<Localizacao> localizacoes;
    private TextView mTopText;

    /** Demonstrates customizing the info window and/or its contents. */
    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;
        private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
        private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 110;


        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

            //obtem permissão pra acessar os contatos
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }


        }

        @Override
        public View getInfoWindow(Marker marker) {
            //TODO
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            //TODO
            return null;
        }

        private void render(Marker marker, View view) {

            ((ImageView) view.findViewById(R.id.badge)).setImageResource(0);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Insere as localizações no mapa
        addMarkersToMap();

        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Map with lots of markers.");


        LatLngBounds.Builder builderBounds = new LatLngBounds.Builder();
        if(localizacoes.size() > 0) {
            //centraliza nos pontos


            for (int i = 0; i < localizacoes.size(); i++) {
                builderBounds.include(new LatLng(new Double(localizacoes.get(i).latitude), new Double(localizacoes.get(i).longitude)));
            }


        }else{
            //senão, centraliza no brasil
            builderBounds.include(new LatLng(-35.71, -74.01));
            builderBounds.include(new LatLng( 2.04, -35.28));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builderBounds.build(), 200));
    }

    private void addMarkersToMap() {
        //alterações para adicionar marcadores do banco
        localizacoes  = this.getLocalizacoes();

        for (int i = 0; i < localizacoes.size(); i++) {


            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(new Double(localizacoes.get(i).latitude), new Double(localizacoes.get(i).longitude)))
                    .icon(BitmapDescriptorFactory.fromBitmap(localizacoes.get(i).foto))
                    .title(localizacoes.get(i).toString()));
        }

    }

    public List<Localizacao> getLocalizacoes(){
        DatabaseHelperLocalizacao db  = new DatabaseHelperLocalizacao(this);
        ArrayList<Localizacao>  localizacoes = db.getAll();

        return localizacoes;

    }


    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!checkReady()) {
            return;
        }
        float rotation = seekBar.getProgress();
        for (Marker marker : mMarkerRainbow) {
            marker.setRotation(rotation);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Do nothing.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Do nothing.
    }


    // Método para clicar na imagem (ponto) do mapa
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Markers have a z-index that is settable and gettable.
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        Toast.makeText(this, marker.getTitle() + " z-index set to " + zIndex, Toast.LENGTH_SHORT).show();

        onClickedImage(marker.getTitle().replaceAll("[^0-9]", "")); // chama método responsável para realizar ligação


        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Toast.makeText(this, "Info Window long click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        mTopText.setText("onMarkerDragStart");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mTopText.setText("onMarkerDragEnd");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        mTopText.setText("onMarkerDrag.  Current Position: " + marker.getPosition());
    }


    // Método que realiza ligações quando a imagem for clicada
    public void onClickedImage(String telefone) {

        Uri uri = Uri.parse("tel: " + telefone);

        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }
        startActivity(intent);
    }

    /**
     * Ao retornar de uma chamada para adicionar uma localização, vai limpar o mapa e readicionar os marcadores
     */
    @Override
    public void onStart(){
        super.onStart();
        if(mMap != null) {
            mMap.clear();
            addMarkersToMap();
        }

    }

    /**
     * Chamado ao clicar no botão de adicionar localização
     * */
    public void onAdicionar(View view) {
        startActivity(new Intent(this, NovaLocalizacaoComFotoActivity.class));;
    }

    /**
     * Chamado ao clicar no botão de adicionar localização
     * */
    public void onListar(View view) {
        startActivity(new Intent(this, ListarLocalizacoesActivity.class));;
    }

}
