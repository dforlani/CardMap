package com.andreediogo.cardmap.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andreediogo.cardmap.R;
import com.andreediogo.cardmap.database.DatabaseHelperLocalizacao;
import com.andreediogo.cardmap.model.ContactsResolver;
import com.andreediogo.cardmap.model.EntidadeContato;
import com.andreediogo.cardmap.model.Localizacao;

import java.util.ArrayList;
import java.util.List;

public class NovaLocalizacaoComFotoActivity extends AppCompatActivity
        implements OnClickListener, LocationListener {

    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;
    private LocationManager mLocationManager;

    private Button btnSalvar = null;
    private ProgressBar pb = null;

    private static final String TAG = "Debug";
    private Boolean flag = false;
    private boolean isGpsEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    /**
     * location
     */
    private Location mLocation;

    /**
     * min distance change to get location update
     */
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10;

    /**
     * min time for location update
     * 60000 = 1min
     */
    private static final long MIN_TIME_FOR_UPDATE = 60000;
    //variáveis da câmera
    private static final int CAMERA_REQUEST = 1888;
    private static final int LOCALIZACAO_REQUEST = 19999;

    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap photo;
    private EditText editTextNome = null;
    private EditText editTextTelefone = null;
    private Context mContext;
    private AutoCompleteTextView comboContatos = null;

    //lista de contatos para combo dos contatos
    private List<EntidadeContato> contatos = new ArrayList<>();
    private EntidadeContato contatoSelecionado = null;

    private Double novaLatitude = 0d;
    private Double novaLongitude = 0d;

    Localizacao localizacaoEdicao = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_localizacao_com_foto);
        this.mContext = this;


        //obtem permissão pra acessar a localização do GPS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }

        //if you want to lock screen for always Portrait mode
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);


        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(this);

        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);


        //constroi o layout da câmera
        this.imageView = (ImageView) this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        //inicia a combo dos contatos
        ContactsResolver ctc = new ContactsResolver(this);
        contatos = ctc.getContatos("");
        AutoCompleteTextView comboContatos = findViewById(R.id.combo_contatos);

        //evento para setar contatoSecionaddo quando for clicado na combo
        comboContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                contatoSelecionado = contatos.get(pos);
            }
        });

        AutoCompleteContatosAdapter adapter = new AutoCompleteContatosAdapter(this, contatos);
        comboContatos.setAdapter(adapter);

        //coletar informações da localização para edição vindas de outra activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String idlocalizacaoEdicao = extras.getString("ID_LOCALIZACAO");
            if (idlocalizacaoEdicao != null) {
                DatabaseHelperLocalizacao database = new DatabaseHelperLocalizacao(this.getBaseContext());
                localizacaoEdicao = database.getOneByID(idlocalizacaoEdicao);
                if (localizacaoEdicao != null) {
                    imageView.setImageBitmap(localizacaoEdicao.foto);
                    photo = localizacaoEdicao.foto;

                    comboContatos.setText(localizacaoEdicao.nome, false);
                    contatoSelecionado = localizacaoEdicao.contato;

                    novaLatitude = localizacaoEdicao.latitude;
                    novaLongitude = localizacaoEdicao.longitude;

                    btnSalvar.setEnabled(true);
                }
            }
        }

        Button bttSelecionarLocalizacao = findViewById(R.id.btt_selecionar_localizacao);
        bttSelecionarLocalizacao.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), SelecionarLocalizacaoActivity.class);
                        startActivityForResult(intent, LOCALIZACAO_REQUEST);

                    }
                }

        );


    }



    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        flag = displayGpsStatus();
        if (!flag) {
            Toast.makeText(getBaseContext(), "Seu GPS está desligado e não foi possível salvar", Toast.LENGTH_SHORT).show();
        } else if (isFormularioValido()) {

            //   pb.setVisibility(View.VISIBLE);
            pb.setVisibility(View.INVISIBLE);
            //gera a localização atual
            geraLocalizacao();

            //nova localização
            if (localizacaoEdicao == null) {
                //cria um objeto de localização
                Localizacao nova_localizacao = new Localizacao(novaLatitude,
                        novaLongitude,
                        photo,
                        new Integer(contatoSelecionado.getID()),
                        null
                );

                DatabaseHelperLocalizacao database = new DatabaseHelperLocalizacao(this);
                database.add(nova_localizacao);
            } else {//edição de localização
                localizacaoEdicao.contato = contatoSelecionado;
                localizacaoEdicao.latitude = novaLatitude;
                localizacaoEdicao.longitude = novaLongitude;
                localizacaoEdicao.foto = photo;
                localizacaoEdicao.idContato = new Integer(contatoSelecionado.getID());

                DatabaseHelperLocalizacao database = new DatabaseHelperLocalizacao(this);
                database.update(localizacaoEdicao);
            }

            Toast.makeText(getBaseContext(), "Contato salvo com sucesso", Toast.LENGTH_SHORT).show();
            finish();

        }

    }

    public boolean isFormularioValido() {
        boolean isValidado = true;
        if (photo == null) {
            Toast.makeText(getBaseContext(), "Tire uma foto para identificar a localização", Toast.LENGTH_SHORT).show();
            isValidado = false;
        }

        if (contatoSelecionado == null) {
            Toast.makeText(getBaseContext(), "Selecione um contato para a localização", Toast.LENGTH_SHORT).show();
            isValidado = false;
        }
        return isValidado;
    }

    /****************   FUNÇÕES DO GPS  *************************/

    public double getLatitude() {

        if (mLocation != null) {

            return mLocation.getLatitude();
        }
        return 0;
    }

    public double getLongitude() {

        if (mLocation != null) {

            return mLocation.getLongitude();

        }

        return 0;
    }

    public Location geraLocalizacao() {
        try {

            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //obtem o status do GPS
            isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //obtem o status do provedor de localização pela rede
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGpsEnabled && !isNetworkEnabled) {
                //não vai ser possível obter nenhuma localização

            } else {
                this.canGetLocation = true;

                //obtem coordenadas pelo GPS
                if (isGpsEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);

                    if (mLocationManager != null) {
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    } else
                        //obtem coordenadas pelo provedor de rede
                        if (isNetworkEnabled) {

                            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);

                            if (mLocationManager != null) {
                                mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }
                        }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mLocation;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Verifica se o GPS está ligado
     *
     * @return
     */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }
    /************** FIM DAS FUNÇÕES DO GPS          ***************************/


    /***************************   FUNÇÕES DA CÂMERA **************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**************** FIM DAS FUNÇÕES DA CÂMERA           ********************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }else{
            if(requestCode == LOCALIZACAO_REQUEST && resultCode == Activity.RESULT_OK){
                novaLatitude = data.getDoubleExtra("latitude", 0);
                novaLongitude = data.getDoubleExtra("longitude", 0);
                if(novaLatitude != 0 && novaLongitude != 0){
                    btnSalvar.setEnabled(true);
                }
            }
        }
    }

}