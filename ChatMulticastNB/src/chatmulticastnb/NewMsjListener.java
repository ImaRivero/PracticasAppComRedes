/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatmulticastnb;

import shared.Usuario;

/**
 *
 * @author omari
 */
public interface NewMsjListener {
    void nuevoMensajeHandler(NewMsjEvent e);    
    void userRemoved(Usuario u);    
    void userAdded(Usuario u);
}
