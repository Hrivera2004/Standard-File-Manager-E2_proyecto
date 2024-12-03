/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estru2_proyecto;

import java.io.Serializable;

/**
 *
 * @author hriverav
 */
public class Campo {
    //tipo: 0 = boolean, 1 = int, 2 = float, 3 = string, 4 = char
    //Longitud maxima 256    //Longitud maxima 256
    int tipo, longitud;
    String nombre_campo;
    boolean iskey;
    boolean iskey_secundary;
    
    public Campo() {
    }

    public Campo(String nombre_campo, int tipo, int longitud, boolean iskey, boolean iskeay_secundary) {
        this.tipo = tipo;
        this.longitud = longitud;
        this.nombre_campo = nombre_campo;
        this.iskey = iskey;
        this.iskey_secundary = iskey;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public String getNombre_campo() {
        return nombre_campo;
    }

    public void setNombre_campo(String nombre_campo) {
        this.nombre_campo = nombre_campo;
    }

    public boolean isIskey() {
        return iskey;
    }

    public void setIskey(boolean iskey) {
        this.iskey = iskey;
    }

    public void modify(int tipo, int longitud, String nombre_campo, boolean iskey) {
        this.tipo = tipo;
        this.longitud = longitud;
        this.nombre_campo = nombre_campo;
        this.iskey = iskey;
    }

    @Override
    public String toString() {
        return nombre_campo + "-" + tipo + "-" + longitud + "-" + ((iskey) ? "1" : "0") + "-"+((iskey_secundary) ? "1" : "0")+'|';
    }

}
