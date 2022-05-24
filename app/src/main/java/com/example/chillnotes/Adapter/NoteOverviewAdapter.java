package com.example.chillnotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillnotes.ConexionSQLiteHelper;
import com.example.chillnotes.Entity.Nota;
import com.example.chillnotes.IntroducirDatosActivity;
import com.example.chillnotes.R;
import com.example.chillnotes.Utilidades;

import java.util.List;

public class NoteOverviewAdapter extends RecyclerView.Adapter<NoteOverviewAdapter.ViewHolder>
    implements View.OnClickListener {

    private List<Nota> noteList;
    private String usuario;
    private Context context;
    private View.OnClickListener listener;

    public NoteOverviewAdapter(List<Nota> noteList, String pUsuario, Context context) {
        this.noteList = noteList;
        this.context = context;
        this.usuario = pUsuario;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_notas, parent, false);
        view.setOnClickListener(this);
        Button btnColor = view.findViewById(R.id.btn_color);
        btnColor.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Nota miNota = noteList.get(position);
        holder.txtTitulo.setText(noteList.get(position).getTitulo());
        holder.txtResumen.setText(noteList.get(position).getResumen());
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent miIntent = new Intent(context, IntroducirDatosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("nota", miNota);
                miIntent.putExtras(bundle);
                miIntent.putExtra("user", usuario);

                context.startActivity(miIntent);

            }
        });
        holder.btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                miNota.rotateColor();
                registrarNota(miNota);
                //Intent miIntent = new Intent(context, MainActivity.class);
                //miIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ((Activity) context).recreate();
                //context.startActivity(miIntent);
                */
                Toast.makeText(context, "Disponible en la siguiente actualizacion", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void registrarNota(Nota miNota) {

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "bd_notas", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();


        db.execSQL("UPDATE " + Utilidades.TABLA_NOTA +
                " SET " + Utilidades.CAMPO_COLOR + " = " + Integer.toString(miNota.getColor()) +
                " WHERE " + Utilidades.CAMPO_FECHA + " = " + Long.toString(miNota.getFechaCreacion()) +
                " AND " + Utilidades.CAMPO_USUARIO + " = " + usuario);
        db.close();
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setOnClickListener(View.OnClickListener pListener) {
        this.listener = pListener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgFoto;
        private TextView txtTitulo;
        private TextView txtResumen;
        private Button btnEditar;
        private Button btnColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.img_foto);
            txtTitulo = itemView.findViewById(R.id.txt_titulo);
            txtResumen = itemView.findViewById(R.id.txt_resumen);
            btnColor = itemView.findViewById(R.id.btn_color);
            btnEditar = itemView.findViewById(R.id.btn_editar);
        }
    }
}
