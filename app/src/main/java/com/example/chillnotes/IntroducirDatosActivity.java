package com.example.chillnotes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.chillnotes.Entity.Nota;

import java.util.Date;

public class IntroducirDatosActivity extends AppCompatActivity {

    EditText campoTitulo, campoTexto;
    Nota miNota;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducir_datos);

        //Inicializaciones
        campoTitulo = (EditText) findViewById(R.id.campo_titulo);
        campoTexto = (EditText) findViewById(R.id.campo_texto);

        //Recuperamos info del intent
        Bundle objetoEnviado = getIntent().getExtras();
        miNota = (Nota) objetoEnviado.getSerializable("nota");
        user = getIntent().getStringExtra("user");
        if (miNota != null) {
            //Establecemos datos en los campos si estamos editando una nota existente
            campoTexto.setText(miNota.getTexto());
            campoTitulo.setText(miNota.getTitulo());
        }



    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_guardar:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Guardar nota")
                    .setMessage("La nota se guardará");
                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        registrarNota();
                        //makeNotification();
                        Toast.makeText(getApplicationContext(),
                                "Guardado",
                                Toast.LENGTH_SHORT).show();
                        notificar("¡Felicidades!", "Guardaste una nueva nota");
                        finish();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();;
                break;
            case R.id.btn_volver:

                finish();

        }

    }

    private void notificar(String titulo, String texto) { //Crea una notificación
        System.out.println("Notificacion");
        int id = (int) System.currentTimeMillis();

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, String.valueOf(id))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(titulo)
                .setContentText(texto)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup("NEW_NOTES")
                .setGroupSummary(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(String.valueOf(id), "Notificacion",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(canal);
        }
        manager.notify(id, builder.build());
    }


    private void registrarNota() { //Registra la nota en la base de datos local
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_notas", null, 2);

        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();

        String tit = campoTitulo.getText().toString();
        String txt = campoTexto.getText().toString();

        values.put(Utilidades.CAMPO_TITULO, tit);
        values.put(Utilidades.CAMPO_TEXTO, txt);

        if (miNota != null) { //Si se esta editando la nota
            String[] fecha = {Long.toString(miNota.getFechaCreacion())};
            values.put(Utilidades.CAMPO_COLOR, miNota.getColor());
            db.update(Utilidades.TABLA_NOTA, values, Utilidades.CAMPO_FECHA + "=?", fecha);
            db.execSQL("UPDATE " + Utilidades.TABLA_NOTA +
                    " SET " + Utilidades.CAMPO_TITULO + " = '" + tit + "' ," +
                    Utilidades.CAMPO_TEXTO + " = '" + txt +
                    "' WHERE " + Utilidades.CAMPO_FECHA + " = " + Long.toString(miNota.getFechaCreacion()) +
                    " AND " + Utilidades.CAMPO_USUARIO + " = '" + user + "'");

        }
        else { //Si es una nota nueva
            values.put(Utilidades.CAMPO_FECHA, new Date().getTime());
            values.put(Utilidades.CAMPO_COLOR, 0);
            values.put(Utilidades.CAMPO_USUARIO, user);
            db.insert(Utilidades.TABLA_NOTA, null, values);
        }
        db.close();
    }
}
