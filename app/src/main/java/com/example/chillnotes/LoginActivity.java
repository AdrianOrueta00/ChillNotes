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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText campoEmail, campoPass;
    Button btnAcceder, btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //inicializaciones
        campoEmail = (EditText) findViewById(R.id.campo_email_acceso);
        campoPass = (EditText) findViewById(R.id.campo_pass_acceso);
        btnAcceder = findViewById(R.id.btn_acceder);
        btnRegistrarse = findViewById(R.id.btn_registrarse);

    }

    private boolean comprobar(String pEmail, String pPass){ //Determina si existe un determinado usuario con cierta contraseña
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_notas", null, 2);

        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_USUARIO +
                " WHERE " + Utilidades.CAMPO_USUARIO + " = '" + pEmail +
                "' AND " + Utilidades.CAMPO_CONTRASENA + " = '" + pPass + "'", null);

        System.out.println("La consulta devuelve " + String.valueOf(cursor.getCount()) + " lineas");

        return (cursor.moveToNext());

    }


    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.btn_acceder: //Si se pulsa el botón de inicio de sesión
                String email = campoEmail.getText().toString();
                String pass = campoPass.getText().toString();

                if (email.trim().length()==0 || pass.trim().length()==0 || !comprobar(email, pass)) { //Si el login es incorrecto
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Inicio de sesión")
                            .setMessage("Ha habido un error con tus credenciales");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else { //Si el login es correcto
                    Intent miIntent = new Intent(LoginActivity.this, MainMenu.class);
                    miIntent.putExtra("user", email);
                    startActivity(miIntent);
                }
                break;
            case R.id.btn_registrarse: //Si se pulsa el botón de registro


                Intent miIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(miIntent);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) { //Guardamos el texto de los campos

        outState.putString("email", campoEmail.getText().toString());
        outState.putString("pass", campoPass.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) { //Recuperamos el texto de los campos

        super.onRestoreInstanceState(savedInstanceState);

        String email = savedInstanceState.getString("email");
        String pass = savedInstanceState.getString("pass");

        campoEmail = (EditText) findViewById(R.id.campo_email_acceso);
        campoPass = (EditText) findViewById(R.id.campo_pass_acceso);

        campoEmail.setText(email);
        campoPass.setText(pass);
    }

}
