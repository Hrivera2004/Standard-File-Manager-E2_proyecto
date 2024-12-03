/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estru2_proyecto;

import java.awt.Color;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author hriverav
 */
//tipo: 0 = boolean, 1 = int, 2 = float, 3 = string, 4 = char
//500 bytes
public class Metadata {

    private int RRN_headAvail = -1;
    //Solo 10 porque es una cantidad rasonable
    private ArrayList<Campo> campos;
    private int KeyElement = -1;
    private int[] KeyElements_Secundary = {-1, -1};

    public Metadata(int RRN_headAvail, ArrayList<Campo> campos, int KeyElement, int KeyElement_s1, int KeyElement_s2) {
        this.RRN_headAvail = RRN_headAvail;
        this.campos = campos;
        this.KeyElement = KeyElement;
        this.KeyElements_Secundary[0] = KeyElement_s1;
        this.KeyElements_Secundary[1] = KeyElement_s2;
    }

    public Metadata() {
    }

    public ArrayList<Campo> getCampos() {
        return campos;
    }

    public void setCampos(ArrayList<Campo> campos) {
        this.campos = campos;
    }

    public int getKeyElement_pos() {
        return KeyElement;
    }

    public Campo getKeyElement() {
        return campos.get(KeyElement);
    }

    public void setKeyElement_pos(int KeyElement) {
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
            if (campo.isIskey_secundary()) {
                if (KeyElements_Secundary[0] != -1) {
                    if (KeyElements_Secundary[1] != -1) {
                        int option = JOptionPane.showConfirmDialog(null, "Ya tiene 2 llaves secundarias\nDesea replazarla 1 por la nueva llave que introdujo?.", "Error: Ya hay una key principal", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {

                            Object[] options = {campos.get(KeyElements_Secundary[0]), campos.get(KeyElements_Secundary[1])};

                            // Display the JOptionPane
                            int result = JOptionPane.showOptionDialog(
                                    null,
                                    "Cual campo desea cambiar:",
                                    "Opciones",
                                    JOptionPane.YES_NO_OPTION, // Use YES_NO_OPTION type for simplicity
                                    JOptionPane.QUESTION_MESSAGE, // Icon type
                                    null, // Default icon
                                    options, // Custom options
                                    options[1] // Default option (highlighted)
                            );
                            if (result == 0) {
                                campo.setIskey_secundary(true);
                                campos.set(KeyElements_Secundary[0], campo);
                            } else if (result == 1) {
                                campo.setIskey_secundary(true);
                                campos.set(KeyElements_Secundary[1], campo);
                            } else {
                                JOptionPane.showMessageDialog(null, "Error: El campo se va a añadir como campo normal");
                                campo.setIskey_secundary(false);
                                campo.setIskey_secundary(false);
                            }
                        } else {
                            campo.setIskey_secundary(false);
                            campo.setIskey(false);
                        }
                    } else {
                        KeyElements_Secundary[1] = 1;
                    }
                } else {
                    KeyElements_Secundary[0] = 1;
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
        String temp1 = "";
        for (Campo campo : campos) {
            temp1 += campo.toString();
        }

        return ((RRN_headAvail == -1) ? -1 : " " + RRN_headAvail) + ";" + temp1 + ";" + KeyElement + ";" + KeyElements_Secundary[0] + ";" + KeyElements_Secundary[1] + ";";
    }

    public ArrayList<String> getKeys() {
        ArrayList<String> keys = new ArrayList();
        if (KeyElement != -1) {
            keys.add(campos.get(KeyElement).getNombre_campo() + "-" + KeyElement);
        }
        if (KeyElements_Secundary[0] != -1) {
            keys.add(campos.get(KeyElements_Secundary[0]).getNombre_campo() + "-" + KeyElements_Secundary[0]);
        }
        if (KeyElements_Secundary[1] != -1) {
            keys.add(campos.get(KeyElements_Secundary[1]).getNombre_campo() + "-" + KeyElements_Secundary[1]);
        }
        return keys;
    }

}
