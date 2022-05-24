package com.example.chillnotes.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Nota implements Serializable {
    public int getColor() {
        return color;
    }

    public void setColor(int pColor) { //Método para desarrollo futuro
        if (pColor != 0) {
            this.color = pColor-1;
        }
        else {
            pColor = 2;
        }

        this.foto = this.rotateColor();
    }

    private final long fechaCreacion;
    private String titulo;
    private String texto;
    private String foto;
    private int color;

    public Nota(long pFechaCreacion) {
        this.fechaCreacion = pFechaCreacion;
        this.color = 0;
        this.foto = rotateColor();
        this.titulo = "";
        this.texto = "";
    }

    public void setTitulo(String pTitulo) {
        this.titulo = pTitulo;
    }

    public void setTexto(String pTexto) {
        this.texto = pTexto;
    }

    public long getFechaCreacion() {
        return fechaCreacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTexto() {
        return texto;
    }

    public String getResumen() { //Devuelve los primeros 40 caracteres del texto
        String res;
        if (texto != null) {
            if (texto.length() < 40) {
                res = texto.substring(0, texto.length());
            }
            else {
                res = texto.substring(0, 40) + "...";
            }
        }
        else {
            res = "";
        }
        return res;
    }

    public String getFoto() {
        return foto;
    } //Método para desarrollo futuro


    public String rotateColor() { //Método para desarrollo futuro
        ArrayList<String> colores = new ArrayList<String>();
        colores.add("https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/Red.svg/512px-Red.svg.png");
        colores.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/Solid_blue.svg/512px-Solid_blue.svg.png");
        colores.add("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/ICS_Quebec.svg/600px-ICS_Quebec.svg.png");

        String res = colores.get(color);
        if (color < colores.size() - 1) {
            color++;
        }
        else {
            color = 0;
        }
        return res;
    }
}
