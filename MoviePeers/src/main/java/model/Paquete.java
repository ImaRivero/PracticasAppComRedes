/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author omari
 */
public class Paquete implements Serializable{
    
    public static final int NUEVA_TRANSMICION = 1;
    public static final int MENSAJE = 2;
    public static final int LISTA_TRANSMICION = 3;
    public static final int FIN_TRANSMICION = 4;
    
    int tipo;
    Object content;
    Usuario origen;

    public Paquete(int tipo, Object content, Usuario origen) {
        this.tipo = tipo;
        this.content = content;
        this.origen = origen;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Usuario getOrigen() {
        return origen;
    }

    public void setOrigen(Usuario origen) {
        this.origen = origen;
    }
    
    
}
