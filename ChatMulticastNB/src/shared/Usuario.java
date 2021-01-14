/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.net.InetAddress;

/**
 *
 * @author omari
 */
public class Usuario {
    
    private InetAddress ip;
    int puerto;
    private String nombre;

    public Usuario(InetAddress ip, int puerto, String nombre) {
        this.ip = ip;
        this.puerto = puerto;
        this.nombre = nombre;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public boolean equals(Object o) {
        Usuario u = (Usuario) o;
        return ip.equals(u.ip);
    }

    @Override
    public String toString() {
        return nombre + "@" + ip.getHostAddress();
    }
}
