/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estru2_proyecto;

import java.io.Serializable;

/**
 *
 * @author pepes
 */
public class BTreeNode implements Serializable {

    private Comparable[] keys;
    private BTreeNode[] children;
    private boolean isLeaf;
    private int t;
    int n;

    public BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.isLeaf = leaf;
        keys = new Comparable[2 * t - 1];
        children = new BTreeNode[2 * t];
        this.n = 0;
    }

    public Comparable[] getKeys() {
        return keys;
    }

    public BTreeNode[] getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getDegree() {
        return t;
    }

    public int getNumKeys() {
        return n;
    }

    public void setNumKeys(int n) {
        this.n = n;
    }

    public int binarySearch(Object key) {
        int left = 0, right = n - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            Comparable<Object> midKey = (Comparable<Object>) keys[mid];
            Comparable<Object> searchKey = (Comparable<Object>) key;

            int cmp = midKey.compareTo(searchKey);

            if (cmp == 0) {
                return mid; // Clave encontrada
            } else if (cmp < 0) {
                left = mid + 1; // Buscar en la mitad derecha
            } else {
                right = mid - 1; // Buscar en la mitad izquierda
            }
        }

        return -(left + 1); // Retorna la posición de inserción como valor negativo
    }

    public String toString() {
        if (n == 0) {
            return "Nodo vacío"; // O cualquier mensaje que te ayude a ver que el nodo está vacío
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(keys[i]).append(" ");
        }
        return sb.toString().trim();  // Devuelve las claves como una cadena separada por espacios
    }

    void setNumberKeys(int length) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
