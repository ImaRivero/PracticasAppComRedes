/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author omari
 */
public class Sala implements Serializable{
    
    private String nombre;
    private String descripcion;
    private boolean publica;
    private ArrayList<String> genero;
    private String path;
    private String code;
    private String ip;
    private int port;
    

    public Sala(){
        
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isPublica() {
        return publica;
    }

    public void setPublica(boolean tipo) {
        this.publica = tipo;
    }

    public ArrayList<String> getGenero() {
        return genero;
    }

    public void setGenero(ArrayList<String> genero) {
        this.genero = genero;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    
    public String movieCardFormat(){
        String out = "<html><b>Título: " + this.nombre + "</b><br>"
                     +"<b>Descripcion:</b><br>"
                     + this.descripcion 
                     +"</html>";
        return out;
    }
}
