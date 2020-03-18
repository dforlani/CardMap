package com.example.mapdemo.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mapdemo.model.Localizacao
import io.github.cerveme.crud_kotlin.model.Configuracao
import java.util.*



class DatabaseHelperConfiguracao(context: Context,
                                 factory: SQLiteDatabase.CursorFactory?) :
        SQLiteOpenHelper(context, DATABASE_NAME,
                factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT," +
                COLUMN_LATITUDE + " TEXT," +
                COLUMN_LONGITUDE + " TEXT,"
                + ")")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    public fun dropDatabase() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun add(localizacao: Localizacao) {
        val values = ContentValues()
//        values.put(COLUMN_ID, localizacao.tipo)
        values.put(COLUMN_LATITUDE, localizacao.latitude)
        values.put(COLUMN_LONGITUDE, localizacao.longitude)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAll(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

//    fun getCodigoClienteApp(): String {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE TIPO LIKE 'cod_cliente_app'", null)
//        if(cursor!!.count > 0) {
//            cursor!!.moveToFirst()
//            do {
//                return cursor.getString(cursor.getColumnIndex(DatabaseHelperConfiguracao.COLUMN_NAME))
//            } while (cursor.moveToNext())
//        }else{
//
//            //gera novo código de aplicativo e salva no banco
//            val randomString = "oi" //Random().nextInt(100000, 999999)
//            val newConf = Configuracao(randomString,"cod_cliente_app" )
//            val oi = "oi"
//            addConfiguracao(newConf)
//
//            return randomString
//        }
//        cursor.close()
//    }

    fun deleteAll() {
        val values = ContentValues()

        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }



//    fun hasCodigoCadastrado(): Boolean {
//        val cursor = getAllConfiguracoes()
//        return cursor!!.count > 0
//
//    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "localizacao.db"
        val TABLE_NAME = "localizacao"
        val COLUMN_ID = "id_localizacao"
        val COLUMN_LATITUDE = "latitude"
        val COLUMN_LONGITUDE = "longitude"
    }
}