/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estru2_proyecto;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author hriverav
 */
//tipo: 0 = boolean, 1 = int, 2 = float, 3 = string, 4 = char
//500 bytes
public class Metadata {

    private int RRN_headAvail = -1;
    //Solo 10 porque es una cantidad rasonable
    //la posicion de campos pertenece a la misma posicion de tipos
    private ArrayList<Campo> campos;
    private int KeyElement = -1;

    public Metadata(int RRN_headAvail, ArrayList<Campo> campos, int KeyElement) {
        this.RRN_headAvail = RRN_headAvail;
        this.campos = campos;
        this.KeyElement = KeyElement;
    }

    public Metadata() {
    }

    public ArrayList<Campo> getCampos() {
        return campos;
    }

    public void setCampos(ArrayList<Campo> campos) {
        this.campos = campos;
    }

    public int getKeyElement() {
        return KeyElement;
    }

    public void setKeyElement(int KeyElement) {
        this.KeyElement = KeyElement;
    }

    public int getRRN_headAvail() {
        return RRN_headAvail;
    }

    public void setRRN_headAvail(int RRN_headAvail) {
        this.RRN_headAvail = RRN_headAvail;
    }

    public void add_campos(Campo campo) {
        if (campos.size() < 10) {
            if (campo.iskey) {
                if (KeyElement == -1) {
                    KeyElement = campos.size();
                } else {
                    int option = JOptionPane.showConfirmDialog(null, "Ya tiene un llave principal\nDesea replazarla por la nueva llave que introdujo?.", "Error: Ya hay una key principal", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        Campo oldKeyCampo = campos.get(KeyElement); // Guardamos el campo que será reemplazado
                        oldKeyCampo.setIskey(false); // Desmarcar el campo anterior como llave principal
                        KeyElement = campos.size();
                    } else {
                        campo.setIskey(false);
                    }
                }
            }
            campos.add(campo);
        }
    }

    public void edit_campo(int pos, int tipo, int longitud, String nombre, boolean key) {
        if (pos < campos.size()) {
            if (KeyElement == pos) {
                campos.get(pos).modify(tipo, longitud, nombre, key);
                if (!key) {
                    KeyElement = -1;
                }
            } else {
                if (key && KeyElement != -1) {
                    int option = JOptionPane.showConfirmDialog(null, "Ya tiene un llave principal\nDesea replazarla por la nueva llave que introdujo?.", "Error: Ya hay una key principal", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        campos.get(KeyElement).setIskey(false); // Desmarcar el campo anterior como llave principal
                        KeyElement = pos;
                    } else {
                        key = false;
                    }
                } else if (key) {
                    KeyElement = pos;
                }
                campos.get(pos).modify(tipo, longitud, nombre, key);
            }
        } else {
            JOptionPane.showMessageDialog(null, "El registro indicado no existe", "Error: out of bounds", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public String toString() {
        String temp = "";
        for (Campo campo : campos) {
            temp += campo.toString();
        }
        return ((RRN_headAvail == -1) ? -1 : " " + RRN_headAvail) + ";" + temp + ";" + KeyElement + ";";
    }

}
