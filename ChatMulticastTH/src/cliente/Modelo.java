/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

/**
 *
 * @author imanol
 */
public class Modelo{
    
    private String nom_user;
    private boolean conectado;
    private String txt_chat;

    public String getTxt_chat() {
        return txt_chat;
    }

    public void setTxt_chat(String txt_chat) {
        this.txt_chat = txt_chat;
    }
    
    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public String getNom_user() {
        return nom_user;
    }

    public void setNom_user(String nom_user) {
        this.nom_user = nom_user;
    }
    
    
}
