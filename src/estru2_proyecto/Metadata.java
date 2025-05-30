/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estru2_proyecto;

import java.awt.Color;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
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

    private long RRN_headAvail = -1;
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

    public long getRRN_headAvail() {
        return RRN_headAvail;
    }

    public void setRRN_headAvail(long RRN_headAvail) {
        this.RRN_headAvail = RRN_headAvail;
    }

    public void add_campos(Campo campo) {
        if (campos.size() < 10) {
            // Verificar si es una llave principal
            if (campo.iskey) {
                if (KeyElement == -1) {
                    KeyElement = campos.size();
                } else {
                    int option = JOptionPane.showConfirmDialog(
                            null,
                            "Ya tiene una llave principal\n¿Desea reemplazarla por la nueva llave que introdujo?",
                            "Error: Ya hay una key principal",
                            JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        Campo oldKeyCampo = campos.get(KeyElement); // Guardamos el campo que será reemplazado
                        oldKeyCampo.setIskey(false); // Desmarcar el campo anterior como llave principal
                        KeyElement = campos.size(); // Asignamos la nueva posición de la llave principal
                    } else {
                        campo.setIskey(false);
                    }
                }
            }

            // Verificar si es una llave secundaria
            if (campo.isIskey_secundary()) {
                if (KeyElements_Secundary[0] != -1) {
                    if (KeyElements_Secundary[1] != -1) {
                        int option = JOptionPane.showConfirmDialog(
                                null,
                                "Ya tiene 2 llaves secundarias\n¿Desea reemplazar la 1 por la nueva llave que introdujo?",
                                "Error: Ya hay una key secundaria",
                                JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            Object[] options = {campos.get(KeyElements_Secundary[0]), campos.get(KeyElements_Secundary[1])};
                            int result = JOptionPane.showOptionDialog(
                                    null,
                                    "¿Qué campo desea cambiar?",
                                    "Opciones",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[1]
                            );
                            if (result == 0) {
                                campo.setIskey_secundary(true);
                                campos.set(KeyElements_Secundary[0], campo);
                            } else if (result == 1) {
                                campo.setIskey_secundary(true);
                                campos.set(KeyElements_Secundary[1], campo);
                            } else {
                                JOptionPane.showMessageDialog(null, "El campo se añadirá como campo normal");
                                campo.setIskey_secundary(false);
                            }
                        } else {
                            campo.setIskey_secundary(false);
                        }
                    } else {
                        // Asignar el índice correcto para la segunda llave secundaria
                        KeyElements_Secundary[1] = campos.size();
                    }
                } else {
                    // Asignar el índice correcto para la primera llave secundaria
                    KeyElements_Secundary[0] = campos.size();
                }
            }

            // Añadir el campo a la lista
            campos.add(campo);
        }
    }

    public void edit_campo(int pos, int tipo, int longitud, String nombre, boolean keyPrimaria, boolean keySecundaria) {
        if (pos < campos.size()) {
            Campo campo = campos.get(pos);

            // Validar que no sea llave primaria y secundaria al mismo tiempo
            if (keyPrimaria && keySecundaria) {
                JOptionPane.showMessageDialog(null, "Un campo no puede ser llave primaria y secundaria al mismo tiempo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Manejo de llave primaria
            if (keyPrimaria) {
                if (KeyElement != -1 && KeyElement != pos) {
                    int option = JOptionPane.showConfirmDialog(
                            null,
                            "Ya existe una llave primaria.\n¿Desea reemplazarla por la nueva llave que introdujo?",
                            "Error: Llave primaria existente",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (option == JOptionPane.YES_OPTION) {
                        campos.get(KeyElement).setIskey(false);
                        KeyElement = pos;
                    } else {
                        keyPrimaria = false;
                    }
                } else {
                    KeyElement = pos;
                }
            } else if (KeyElement == pos) {
                KeyElement = -1;
            }

            // Manejo de llaves secundarias
            if (keySecundaria) {
                if (campo.isIskey()) {
                    JOptionPane.showMessageDialog(null, "No se puede asignar una llave secundaria a un campo que ya es llave primaria.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Contar las llaves secundarias existentes
                int secondaryKeyCount = 0;
                for (int key : KeyElements_Secundary) {
                    if (key != -1) {
                        secondaryKeyCount++;
                    }
                }

                if (secondaryKeyCount >= 2) {
                    int option = JOptionPane.showConfirmDialog(null, "Ya existen 2 llaves secundarias.\n¿Desea reemplazar una?", "Error", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // Reemplazar una llave secundaria existente
                        Object[] options = {
                            campos.get(KeyElements_Secundary[0]).getNombre_campo() + " (Pos: " + KeyElements_Secundary[0] + ")",
                            campos.get(KeyElements_Secundary[1]).getNombre_campo() + " (Pos: " + KeyElements_Secundary[1] + ")"
                        };
                        int result = JOptionPane.showOptionDialog(
                                null,
                                "¿Qué llave secundaria desea reemplazar?",
                                "Reemplazo de llave secundaria",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]
                        );

                        if (result == 0) {
                            campos.get(KeyElements_Secundary[0]).setIskey_secundary(false);
                            KeyElements_Secundary[0] = pos;
                        } else if (result == 1) {
                            campos.get(KeyElements_Secundary[1]).setIskey_secundary(false);
                            KeyElements_Secundary[1] = pos;
                        } else {
                            keySecundaria = false;
                        }
                    } else {
                        keySecundaria = false;
                    }
                } else {
                    // Asignar la llave secundaria al primer espacio disponible
                    if (KeyElements_Secundary[0] == -1) {
                        KeyElements_Secundary[0] = pos;
                    } else if (KeyElements_Secundary[1] == -1) {
                        KeyElements_Secundary[1] = pos;
                    }
                }
            } else {
                // Remover la llave secundaria si no es seleccionada
                if (KeyElements_Secundary[0] == pos) {
                    KeyElements_Secundary[0] = -1;
                }
                if (KeyElements_Secundary[1] == pos) {
                    KeyElements_Secundary[1] = -1;
                }
            }

            // Validar y limpiar el estado del arreglo KeyElements_Secundary
            for (int i = 0; i < KeyElements_Secundary.length; i++) {
                if (KeyElements_Secundary[i] >= campos.size()) {
                    KeyElements_Secundary[i] = -1; // Limpiar referencias inválidas
                }
            }

            // Aplicar siempre las modificaciones
            campo.modify(tipo, longitud, nombre, keyPrimaria, keySecundaria);
        }
    }

    @Override
    public String toString() {
        String temp1 = "";
        for (Campo campo : campos) {
            temp1 += campo.toString();
        }

        return ((RRN_headAvail == -1) ? " " : RRN_headAvail) + ";"
                + temp1 + ";"
                + ((KeyElement == -1) ? " " : KeyElement) + ";"
                + ((KeyElements_Secundary[0] == -1) ? " " : KeyElements_Secundary[0]) + ";"
                + ((KeyElements_Secundary[1] == -1) ? " " : KeyElements_Secundary[1]) + ";";

    }

    public HashMap<String, Integer> getKeys() {
        HashMap<String, Integer> keys = new HashMap<>();
        if (KeyElement != -1 && KeyElement < campos.size()) {
            keys.put(campos.get(KeyElement).getNombre_campo(), KeyElement);
        }
        if (KeyElements_Secundary.length > 0 && KeyElements_Secundary[0] != -1 && KeyElements_Secundary[0] < campos.size()) {
            keys.put(campos.get(KeyElements_Secundary[0]).getNombre_campo(), KeyElements_Secundary[0]);
        }
        if (KeyElements_Secundary.length > 1 && KeyElements_Secundary[1] != -1 && KeyElements_Secundary[1] < campos.size()) {
            keys.put(campos.get(KeyElements_Secundary[1]).getNombre_campo(), KeyElements_Secundary[1]);
        }
        return keys;
    }

}
