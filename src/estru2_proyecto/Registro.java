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
    
    private int clave=-1;
    private ArrayList<Object> data = new ArrayList<>();
    private boolean borrado = false;
    private int RRN = -1;
    private int RRN_next = -1;
    
    
    public Registro() {
    }


    public Registro(ArrayList<Object> data,int clave) {
        this.data = data;
        this.clave = clave;
    }

    public Registro(ArrayList<Object> data, boolean borrado, int RRN_next, int clave, int s) {
        this.data = data;
        this.borrado = borrado;
        this.RRN_next = RRN_next;
        this.clave = clave;
    }
    
    public Registro(ArrayList<Object> data, boolean borrado, int RRN, int RRN_next) {
        this.data = data;
        this.borrado = borrado;
        this.RRN = RRN;
        this.RRN_next = RRN_next;
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

    public int getRRN() {
        return RRN;
    }

    public void setRRN(int RRN) {
        this.RRN = RRN;
    }
    

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        
        for (Object dato : data) {
            temp.append(dato).append(";");
        }
        return temp + "|" + borrado + "|" + RRN_next + '|' + clave + '|';
    }
    public Object getclave(){
        return data.get(clave);
    }
}
