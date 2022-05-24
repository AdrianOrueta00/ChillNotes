package com.example.chillnotes;

import static com.example.chillnotes.Utilidades.CREAR_TABLA_NOTA;
import static com.example.chillnotes.Utilidades.CREAR_TABLA_USUARIO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {



    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Creando la tabla usuarios");
        db.execSQL(CREAR_TABLA_USUARIO);
        System.out.println("Creando la tabla nota");
        db.execSQL(CREAR_TABLA_NOTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_NOTA);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_USUARIO);
        onCreate(db);
    }
}
