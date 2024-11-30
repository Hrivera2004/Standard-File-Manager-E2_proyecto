/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estru2_proyecto;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author hriverav
 */
public class BTreeNode {
    Registro[] keys;
    int t;
    BTreeNode[] children;
    int n;
    boolean leaf;

    public BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        keys = new Registro[2 * t - 1];
        children = new BTreeNode[2 * t];
        n = 0;
    }

    public Registro[] getKeys() {
        return keys;
    }

    public void setKeys(Registro[] keys) {
        this.keys = keys;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public BTreeNode[] getChildren() {
        return children;
    }

    public void setChildren(BTreeNode[] children) {
        this.children = children;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    @Override
    public String toString() {
        return "BTreeNode{" + "keys=" + keys + ", t=" + t + ", children=" + children + ", n=" + n + ", leaf=" + leaf + '}';
    }
    
    public boolean compare(Registro key1,Registro key2){
        if ((key1.getclave() instanceof Float) && (key2.getclave() instanceof Float)) {
            return (((Float)key1.getclave()) < ((Float)key2.getclave()));
        }else{
            if (key1.getclave().toString().compareTo(key2.getclave().toString()) > 0) {
                return false;
            }else{
                return true;
            }
        }
    }
    
    public Registro search(Registro key) {
        int left = 0;
        int right = keys.length - 1;
        while(left <= right){
            int mid = left + (right-left)/2;
            if (keys[mid].getclave() == key.getclave()) {
                return keys[mid];
            }
            if (compare(key,keys[mid])) {
                right = mid-1;
            }else{
                left = mid + 1; 
            }
        }
        return null;
    }

}
