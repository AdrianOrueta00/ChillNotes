package com.example.chillnotes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText campoEmail, campoPass1, campoPass2;
    Button btnRegistrarse;

    @Override
    public void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_register);

        //Inicializaciones
        campoEmail = (EditText) findViewById(R.id.campo_email_registro);
        campoPass1 = (EditText) findViewById(R.id.campo_pass_registro1);
        campoPass2 = (EditText) findViewById(R.id.campo_pass_registro2);
        btnRegistrarse = findViewById(R.id.btn_registrarse_registro);
    }


    public void onClick(View view){
        if (!comprobar(campoEmail.getText().toString())){ //Si el usuario no existe
            if (campoPass1.getText().toString().equals(campoPass2.getText().toString())) { //Si el registro es correcto
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_notas", null, 2);

                SQLiteDatabase db = conn.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Utilidades.CAMPO_USUARIO, campoEmail.getText().toString());
                values.put(Utilidades.CAMPO_CONTRASENA, campoPass1.getText().toString());

                db.insert(Utilidades.TABLA_USUARIO, null, values);

                db.close();

                finish();
            }
            else { //Si la contrasena no coincide con la confirmacion
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Registro")
                        .setMessage("La contraseña no coincide");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        else { //Si el usuario ya existe
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Registro")
                    .setMessage("Este usuario ya está registrado");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private boolean comprobar(String pEmail){ //Devuelve true si el usuario existe
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_notas", null, 2);

        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_USUARIO +
                " WHERE " + Utilidades.CAMPO_USUARIO + " = '" + pEmail + "'", null);

        return (cursor.getCount()==1);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) { //Guarda el texto de los campos

        outState.putString("email", campoEmail.getText().toString());
        outState.putString("pass1", campoPass1.getText().toString());
        outState.putString("pass2", campoPass2.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) { //Recupera el texto de los campos

        super.onRestoreInstanceState(savedInstanceState);

        String email = savedInstanceState.getString("email");
        String pass1 = savedInstanceState.getString("pass1");
        String pass2 = savedInstanceState.getString("pass2");

        campoEmail = (EditText) findViewById(R.id.campo_email_registro);
        campoPass1 = (EditText) findViewById(R.id.campo_pass_registro1);
        campoPass2 = (EditText) findViewById(R.id.campo_pass_registro2);

        campoEmail.setText(email);
        campoPass1.setText(pass1);
        campoPass2.setText(pass2);
    }


    }
