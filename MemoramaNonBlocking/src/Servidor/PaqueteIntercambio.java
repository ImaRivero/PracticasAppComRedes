/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.ArrayList;

/**
 *
 * @author omari
 */
public class PaqueteIntercambio {
    /**
     * Tipos de peticiones:
     * 
     * 1. Solicitud de juego solitario
     * 2. Solicitud de juego en parejas
     */
    
    /**
     * Tipos de respuesta:
     */
    
    /**
     * Tipos de objetos según la peticion
     */
    
    private int naturalezaPaquete;
    private ArrayList<Object> elementosPaquete;
    
    public PaqueteIntercambio(int naturalezaPaquete, ArrayList<Object> elementosPaquete) {
        this.naturalezaPaquete = naturalezaPaquete;
        this.elementosPaquete = elementosPaquete;
    }

    public void setNaturalezaPaquete(int naturalezaPaquete) {
        this.naturalezaPaquete = naturalezaPaquete;
    }

    public void setElementosPaquete(ArrayList<Object> elementosPaquete) {
        this.elementosPaquete = elementosPaquete;
    }

    public int getNaturalezaPaquete() {
        return naturalezaPaquete;
    }

    public ArrayList<Object> getElementosPaquete() {
        return elementosPaquete;
    }

}
