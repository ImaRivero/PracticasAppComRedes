/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author imanol
 */
public class Carta extends JButton{
    
    private int id;
    private boolean emparejada = false;
    private int imas;
    private boolean estado;
    
    public Carta(ImageIcon imas){
    	setIcon(escala(imas));
    }
    
    // False indica que esta mostrada la imagen default
    public void setEstado(boolean estado){
        this.estado = estado;
    }

    public boolean getEstado(){
        return this.estado;
    }

    // Guarda el id del icono que utilizará
    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    //
    public void setEmparejada(boolean matched){
        this.emparejada = matched;
    }

    public boolean getEmparejada(){
        return this.emparejada;
    }
    
    public static ImageIcon escala(ImageIcon imas){
        Image img = imas.getImage();
        Image newimg = img.getScaledInstance(55, 60, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scale = new ImageIcon(newimg);
        return scale;
    }
}
