/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estru2_proyecto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author hriverav
 */
//tamaño maximo de 256 bytes
public class Registro {
    private ArrayList<Object> data = new ArrayList<>();
    private boolean borrado = false;
    private long RRN = -1;
    private int RRN_next = -1;

    public Registro() {
    }

    public Registro(ArrayList<Object> data) {
        this.data = data;

    }

    public Registro(ArrayList<Object> data, boolean borrado, int RRN_next) {
        this.data = data;
        this.borrado = borrado;
        this.RRN_next = RRN_next;
    }

    public Registro(ArrayList<Object> data, boolean borrado, int RRN_next, int RRN) {
        this.data = data;
        this.borrado = borrado;
        this.RRN_next = RRN_next;
        this.RRN = RRN;
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
    }

    public boolean isBorrado() {
        return borrado;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    public int getRRN_next() {
        return RRN_next;
    }

    public void setRRN_next(int RRN_next) {
        this.RRN_next = RRN_next;
    }

    public long getRRN() {
        return RRN;
    }

    public void setRRN(long RRN) {
        this.RRN = RRN;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();

        for (Object dato : data) {
            temp.append(dato).append(";");
        }
        
        return temp + "|" + ((borrado) ? "*" : " ") + "|" + ((RRN_next==-1) ? " " : RRN_next) + '|';
    }

}
