package com.example.mapdemo.database;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.mapdemo.model.Localizacao;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHelperLocalizacao extends SQLiteOpenHelper {


// If you change the database schema, you must increment the database version.
     static final int DATABASE_VERSION = 5;
     static final String DATABASE_NAME =  "localizacao.db";
    String TABLE_NAME = "localizacao";
    String COLUMN_ID = "id_localizacao";
    public static String COLUMN_LATITUDE = "latitude";
    public static String COLUMN_LONGITUDE = "longitude";
    public static String COLUMN_FOTO= "foto";
    public static String COLUMN_NOME= "nome";
    public static String COLUMN_TELEFONE= "telefone";


     public DatabaseHelperLocalizacao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
    String CREATE_TABLE = "CREATE TABLE " +
            TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_LATITUDE + " TEXT," +
            COLUMN_LONGITUDE + " TEXT,"+
        COLUMN_FOTO + " BLOB,"+
        COLUMN_NOME + " TEXT,"+
                COLUMN_TELEFONE + " TEXT"
            + ")";



    db.execSQL(CREATE_TABLE);

}



    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public ArrayList<Localizacao> getAll(){
        ArrayList<Localizacao> localizacoes = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor locs = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        if (locs.getCount() > 0){
            locs.moveToFirst();
            Localizacao aux;
            do{
                aux = new Localizacao(locs.getDouble(locs.getColumnIndex(DatabaseHelperLocalizacao.COLUMN_LATITUDE)),
                        locs.getDouble(locs.getColumnIndex(DatabaseHelperLocalizacao.COLUMN_LONGITUDE)),
                        getImage(locs.getBlob(locs.getColumnIndex(DatabaseHelperLocalizacao.COLUMN_FOTO))),
                        locs.getString(locs.getColumnIndex(DatabaseHelperLocalizacao.COLUMN_NOME)),
                        locs.getString(locs.getColumnIndex(DatabaseHelperLocalizacao.COLUMN_TELEFONE))
                );
                localizacoes.add(aux);
            }while(locs.moveToNext());
        }

        return localizacoes;

    }

        public void add(Localizacao loc) {
            // Create and/or open the database for writing
            SQLiteDatabase db = getWritableDatabase();

            // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
            // consistency of the database.
            db.beginTransaction();
            try {
                // The user might already exist in the database (i.e. the same user created multiple posts).


                ContentValues values = new ContentValues();
                values.put(COLUMN_LATITUDE, loc.latitude);
                values.put(COLUMN_LONGITUDE, loc.longitude);
                values.put(COLUMN_FOTO, getBitmapAsByteArray(loc.foto));
                values.put(COLUMN_NOME, loc.nome);
                values.put(COLUMN_TELEFONE, loc.telefone);

                // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
                db.insertOrThrow(TABLE_NAME, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.d("DATABASE", "Error while trying to add post to database");
            } finally {
                db.endTransaction();
            }
        }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Bitmap getImage(byte[] imagem){
if(imagem != null) {
    return BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
}


        return null;
    }
}

