package com.example.chillnotes;

public class Utilidades { //Fichero con las referencias a todos los nombres utilizados en consultas SQL

    public static final String TABLA_NOTA = "nota";
    public static final String TABLA_USUARIO = "usuarios";

    public static final String CAMPO_FECHA = "fecha";
    public static final String CAMPO_TITULO = "titulo";
    public static final String CAMPO_TEXTO = "texto";
    public static final String CAMPO_COLOR = "color";
    public static final String CAMPO_USUARIO = "usuario";
    public static final String CAMPO_CONTRASENA = "contrasena";

    public static final String CREAR_TABLA_NOTA = "CREATE TABLE IF NOT EXISTS " + TABLA_NOTA +
            "(" + CAMPO_FECHA + " LONG PRIMARY KEY, " +
            CAMPO_USUARIO + " TEXT, " +
            CAMPO_TITULO + " TEXT, " +
            CAMPO_TEXTO + " TEXT, " +
            CAMPO_COLOR + " INT, " +
            "FOREIGN KEY(" + CAMPO_USUARIO + ") " +
            "REFERENCES " + TABLA_USUARIO + "(" + CAMPO_USUARIO + "))";

    public static final String CREAR_TABLA_USUARIO = "CREATE TABLE IF NOT EXISTS " + TABLA_USUARIO +
            "(" + CAMPO_USUARIO + " TEXT PRIMARY KEY, " +
            CAMPO_CONTRASENA + " TEXT NOT NULL)";
}
