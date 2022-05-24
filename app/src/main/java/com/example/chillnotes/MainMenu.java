package com.example.chillnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chillnotes.Entity.Nota;
import com.example.chillnotes.Adapter.NoteOverviewAdapter;

import java.util.ArrayList;

import android.database.sqlite.SQLiteOpenHelper;

public class MainMenu extends AppCompatActivity {

    RecyclerView noteRecycler;
    NoteOverviewAdapter noteOverview;
    ArrayList<Nota> listaNotas;
    SQLiteOpenHelper conn;
    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        user = getIntent().getStringExtra("user");

        construirRecycler();

        noteRecycler.setAdapter(noteOverview);

        notificar("¡Hola de nuevo, " + user + "!", "Te echábamos de menos");

    }

    private void consultarListaNotas() { //Realiza la consulta SQL para obtener las notas del usuario
        SQLiteDatabase db = conn.getReadableDatabase();
        Nota note = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_NOTA +
                " WHERE " + Utilidades.CAMPO_USUARIO + " = '" + user + "'", null);

        while (cursor.moveToNext()) {
            note = new Nota(cursor.getLong(0));
            note.setTitulo(cursor.getString(2));
            note.setTexto(cursor.getString(3));
            note.setColor(cursor.getInt(4));

            listaNotas.add(note);
        }
        db.close();
    }


    public void onClick(View view) {
        Intent miIntent = new Intent(MainMenu.this, IntroducirDatosActivity.class);
        miIntent.putExtra("user", user);
        startActivity(miIntent);

    }

    public void onResume() { //Cuando vuelve de editar se actualiza la lista
        super.onResume();
        noteOverview.notifyDataSetChanged();
        construirRecycler();
        noteRecycler.setAdapter(noteOverview);
    }

    private void construirRecycler() { //Construye la lista a mostrar
        noteRecycler = findViewById(R.id.recycler);
        noteRecycler.setLayoutManager(new LinearLayoutManager(this));

        listaNotas = new ArrayList<Nota>();

        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_notas", null, 2);
        consultarListaNotas();


        noteOverview = new NoteOverviewAdapter(listaNotas, user,this);
        noteOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        listaNotas.get(noteRecycler.getChildAdapterPosition(view)).getTitulo(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notificar(String titulo, String texto) {
        System.out.println("Notificacion");
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(titulo)
                .setContentText(texto)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel("1", "Bienvenida",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(canal);
        }
        manager.notify(1, builder.build());
    }

}