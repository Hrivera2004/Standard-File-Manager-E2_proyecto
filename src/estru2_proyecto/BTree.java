/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estru2_proyecto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author pepes
 */
public class BTree implements Serializable {

    private BTreeNode root;
    private int t;

    public BTree(int t) {
        this.t = t;
        this.root = new BTreeNode(t, true);
    }

    public BTree() {

    }

    public BTreeNode search(Object key) {
        return (root == null) ? null : searchInNode(root, key);
    }

    private BTreeNode searchInNode(BTreeNode node, Object key) {
        int index = node.binarySearch(key);

        // Si la clave existe en el nodo
        if (index >= 0 && index < node.getNumKeys() && node.getKeys()[index].getKey().equals(key)) {
            return node;
        }

        // Si es hoja y no está, devolver null
        if (node.isLeaf()) {
            return null;
        }

        // Buscar en el hijo correspondiente
        int childIndex = (index >= 0) ? index : -(index + 1);
        return searchInNode(node.getChildren()[childIndex], key);
    }

    public void insert(Llave key) {
        if (root == null) {
            root = new BTreeNode(t, true);
        }
        if (search(key.getKey()) != null) {
            System.out.println("No pueden haber elementos repetidos");
            return;
        }

            
        if (root.getNumKeys() == 2 * t - 1) {
            BTreeNode newNode = new BTreeNode(t, false);
            newNode.getChildren()[0] = root;
            splitChild(newNode, 0);
            this.root = newNode;
        }

        insertNonFull(root, key);
        System.out.println("Estructura del árbol después de insertar: ");
    }

    // Método para insertar una clave en un nodo que no está lleno
    private void insertNonFull(BTreeNode node, Llave key) {
        int i = node.getNumKeys() - 1;

        if (node.isLeaf()) {
            // Buscar el lugar donde insertar la clave usando búsqueda binaria
            int position = node.binarySearch(key);

            // Asegúrate de que la posición esté dentro del rango
            if (position < 0) {
                position = -position - 1;  // Si no encontramos la clave, obtenemos la posición correcta
            }

            // Insertar la clave en la posición correcta
            for (int j = node.getNumKeys(); j > position; j--) {
                node.getKeys()[j] = node.getKeys()[j - 1];
            }
            node.getKeys()[position] = key;
            node.setNumKeys(node.getNumKeys() + 1);

        } else {
            // Si el nodo no es hoja, buscamos el hijo adecuado
            int position = node.binarySearch(key.getKey());

            // Asegúrate de que la posición esté dentro del rango
            if (position < 0) {
                position = -position - 1;  // Si no encontramos la clave, obtenemos la posición correcta
            }

            // Si el hijo está lleno, lo dividimos antes de insertar
            if (node.getChildren()[position] != null && node.getChildren()[position].getNumKeys() == 2 * t - 1) {
                splitChild(node, position);

                // Después de dividir, el hijo medio se mueve a la posición i del nodo actual
                if (key.getKey().compareTo(node.getKeys()[position].getKey()) > 0) {
                    position++;
                }
            }

            // Llamamos recursivamente en el hijo adecuado
            if (node.getChildren()[position] != null) {
                insertNonFull(node.getChildren()[position], key);  // Insertar en el hijo adecuado
            }
        }
    }

    // Método para dividir un hijo cuando está lleno
    private void splitChild(BTreeNode parent, int index) {
        BTreeNode fullChild = parent.getChildren()[index];
        BTreeNode newChild = new BTreeNode(t, fullChild.isLeaf());  // Crear un nuevo nodo

        // Transferir las claves del nodo lleno al nuevo nodo
        for (int i = 0; i < t - 1; i++) {
            newChild.getKeys()[i] = fullChild.getKeys()[i + t];
        }

        if (!fullChild.isLeaf()) {
            // Transferir los hijos del nodo lleno al nuevo nodo
            for (int i = 0; i < t; i++) {
                newChild.getChildren()[i] = fullChild.getChildren()[i + t];
            }
        }

        // Ajustar el número de claves de ambos nodos
        fullChild.setNumKeys(t - 1);
        newChild.setNumKeys(t - 1);

        // Mover la clave del medio del nodo lleno a la clave de su padre
        for (int i = parent.getNumKeys(); i > index; i--) {
            parent.getKeys()[i] = parent.getKeys()[i - 1];
        }
        parent.getKeys()[index] = fullChild.getKeys()[t - 1];
        parent.setNumKeys(parent.getNumKeys() + 1);

        // Añadir el nuevo hijo al nodo padre
        for (int i = parent.getChildren().length - 1; i > index + 1; i--) {
            parent.getChildren()[i] = parent.getChildren()[i - 1];
        }
        parent.getChildren()[index + 1] = newChild;
    }

    public void printTree() {
        if (root != null) {
            printTreeNode(root, "", true);
        } else {
            System.out.println("El árbol está vacío.");
        }
    }

    private void printTreeNode(BTreeNode node, String indent, boolean isLeft) {
        if (node != null) {
            // Imprimir las claves del nodo
            System.out.println(indent + (isLeft ? "├── " : "└── ") + node.toString());

            // Imprimir los hijos de este nodo
            for (int i = 0; i <= node.getNumKeys(); i++) {
                if (node.getChildren()[i] != null) {
                    printTreeNode(node.getChildren()[i], indent + (isLeft ? "│   " : "    "), i < node.getNumKeys());
                }
            }
        }
    }
    public void delete(Comparable key) {
        if (root == null) {
            System.out.println("El árbol está vacío.");
            return;
        }

        deleteKey(root, key);

        // Si la raíz se queda sin claves y no es hoja, actualizamos la raíz.
        if (root.getNumKeys() == 0 && !root.isLeaf()) {
            root = root.getChildren()[0];
        }
    }

// Método principal para eliminar una clave en un nodo
    private void deleteKey(BTreeNode node, Comparable key) {
        int position = node.binarySearch(key);

        // Caso 1: La clave está en el nodo actual
        if (position < node.getNumKeys() && node.getKeys()[position].getKey().compareTo(key) == 0) {
            if (node.isLeaf()) {
                // Caso 1a: Si el nodo es hoja, simplemente elimina la clave
                removeKey(node, position);
            } else {
                // Caso 1b: Si el nodo no es hoja, buscar predecesor o sucesor
                if (node.getChildren()[position].getNumKeys() >= t) {
                    Llave predecessorKey = findPredecessorKey(node, position);
                    node.getKeys()[position] = predecessorKey;
                    deleteKey(node.getChildren()[position], predecessorKey.getKey());
                } else if (node.getChildren()[position + 1].getNumKeys() >= t) {
                    Llave successorKey = findSuccessorKey(node, position);
                    node.getKeys()[position] = successorKey;
                    deleteKey(node.getChildren()[position + 1], successorKey.getKey());
                } else {
                    // Caso 1c: Ambos hijos tienen menos de t claves, fusionar
                    mergeNodes(node, position);
                    deleteKey(node.getChildren()[position], key);
                }
            }
        } else {
            // Caso 2: La clave no está en este nodo
            if (node.isLeaf()) {
                System.out.println("La clave " + key + " no está en el árbol.");
                return;
            }

            boolean lastChild = (position == node.getNumKeys());
            BTreeNode child = node.getChildren()[position];

            // Asegurarse de que el hijo tenga al menos t claves
            if (child.getNumKeys() < t) {
                fixChild(node, position);
            }

            // Llamada recursiva al hijo correcto
            if (lastChild && position > node.getNumKeys()) {
                deleteKey(node.getChildren()[position - 1], key);
            } else {
                deleteKey(node.getChildren()[position], key);
            }
        }
    }

// Elimina una clave de un nodo hoja
    private void removeKey(BTreeNode node, int index) {
        for (int i = index; i < node.getNumKeys() - 1; i++) {
            node.getKeys()[i] = node.getKeys()[i + 1];
        }
        node.setNumKeys(node.getNumKeys() - 1);
    }

// Encuentra el predecesor de una clave en un nodo
    private Llave findPredecessorKey(BTreeNode node, int index) {//-----------------------------------------------------
        BTreeNode child = node.getChildren()[index];
        while (!child.isLeaf()) {
            child = child.getChildren()[child.getNumKeys()];
        }
        return child.getKeys()[child.getNumKeys() - 1];
    }

// Encuentra el sucesor de una clave en un nodo
    private Llave findSuccessorKey(BTreeNode node, int index) { //-----------------------------------------------------
        BTreeNode child = node.getChildren()[index + 1];
        while (!child.isLeaf()) {
            child = child.getChildren()[0];
        }
        return child.getKeys()[0];
    }

// Fusiona dos nodos en el índice dado
    private void mergeNodes(BTreeNode parent, int index) {
        BTreeNode leftChild = parent.getChildren()[index];
        BTreeNode rightChild = parent.getChildren()[index + 1];

        // Mueve la clave del padre al nodo izquierdo
        leftChild.getKeys()[leftChild.getNumKeys()] = parent.getKeys()[index];
        for (int i = 0; i < rightChild.getNumKeys(); i++) {
            leftChild.getKeys()[leftChild.getNumKeys() + 1 + i] = rightChild.getKeys()[i];
        }

        if (!leftChild.isLeaf()) {
            for (int i = 0; i <= rightChild.getNumKeys(); i++) {
                leftChild.getChildren()[leftChild.getNumKeys() + 1 + i] = rightChild.getChildren()[i];
            }
        }

        // Ajusta el número de claves
        leftChild.setNumKeys(leftChild.getNumKeys() + 1 + rightChild.getNumKeys());

        // Mueve las claves del padre para llenar el vacío
        for (int i = index; i < parent.getNumKeys() - 1; i++) {
            parent.getKeys()[i] = parent.getKeys()[i + 1];
        }

        // Mueve los hijos del padre para llenar el vacío
        for (int i = index + 1; i < parent.getNumKeys(); i++) {
            parent.getChildren()[i] = parent.getChildren()[i + 1];
        }

        parent.setNumKeys(parent.getNumKeys() - 1);
    }

// Arregla un hijo para que tenga al menos t claves
    private void fixChild(BTreeNode parent, int index) {
        BTreeNode child = parent.getChildren()[index];

        if (index > 0 && parent.getChildren()[index - 1].getNumKeys() >= t) {
            moveKey(parent, index - 1, index);
        } else if (index < parent.getNumKeys() && parent.getChildren()[index + 1].getNumKeys() >= t) {
            moveKey(parent, index + 1, index);
        } else {
            if (index < parent.getNumKeys()) {
                mergeNodes(parent, index);
            } else {
                mergeNodes(parent, index - 1);
            }
        }
    }

// Mueve una clave entre nodos adyacentes
    private void moveKey(BTreeNode parent, int fromIndex, int toIndex) {
        BTreeNode fromChild = parent.getChildren()[fromIndex];
        BTreeNode toChild = parent.getChildren()[toIndex];

        if (fromIndex < toIndex) {
            toChild.getKeys()[toChild.getNumKeys()] = parent.getKeys()[fromIndex];
            parent.getKeys()[fromIndex] = fromChild.getKeys()[fromChild.getNumKeys() - 1];
            fromChild.setNumKeys(fromChild.getNumKeys() - 1);
        } else {
            for (int i = toChild.getNumKeys(); i > 0; i--) {
                toChild.getKeys()[i] = toChild.getKeys()[i - 1];
            }
            toChild.getKeys()[0] = parent.getKeys()[fromIndex];
            parent.getKeys()[fromIndex] = fromChild.getKeys()[0];
            fromChild.setNumKeys(fromChild.getNumKeys() - 1);
        }
    }
}
