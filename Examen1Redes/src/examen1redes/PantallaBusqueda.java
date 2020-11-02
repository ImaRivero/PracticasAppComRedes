/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen1redes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.*;

/**
 *
 * @author imanol
 */
public class PantallaBusqueda extends Thread implements ActionListener{
        
    protected JFrame thisframe;
    protected JPanel panel;
    protected JLabel encab, hiloAnt, hiloSig, nombreLabel, msjlabel;
    protected JTextArea mensajes;
    protected JTextField nombreField;
    protected JButton buscar;
    protected GridBagConstraints gbc;
    private PantallaBusqueda ant, sig;
    private String entrada, pathBusc; 
    private int hilosActivos;
    private boolean encontrado = false;
    
    public PantallaBusqueda(int hilosActivos, String pathBusc){
        this.hilosActivos = hilosActivos;
        this.pathBusc = pathBusc;
    }
    
    public void vista(){
        thisframe = new JFrame();
        thisframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisframe.setSize(300, 300);
        thisframe.setTitle("Examen 1 - Aplicaciones Red");
        thisframe.setLocationRelativeTo(null);
        
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.darkGray);
        panel.setOpaque(true);
        thisframe.add(panel);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        
        encab = new JLabel("Soy el hilo: " + Thread.currentThread().getId() + ". Hilos activos: " + hilosActivos, SwingConstants.CENTER);
        encab.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(encab, gbc);
        
        hiloAnt = new JLabel("Hilo anterior: " + ant.getId(), SwingConstants.CENTER);
        hiloAnt.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(hiloAnt, gbc);
        
        hiloSig = new JLabel("Hilo siguiente: " + sig.getId(), SwingConstants.CENTER);
        hiloSig.setForeground(Color.white);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(hiloSig, gbc);
        
        nombreLabel = new JLabel("Ingresa el nombre del arhcivo:", SwingConstants.CENTER);
        nombreLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        panel.add(nombreLabel, gbc);
        
        nombreField = new JTextField(40);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(nombreField, gbc);
        
        buscar = new JButton("Buscar");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(buscar, gbc);
        
        msjlabel = new JLabel("Mensajes:", SwingConstants.CENTER);
        msjlabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(msjlabel, gbc);
        
        mensajes = new JTextArea();
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(mensajes, gbc);
        
        thisframe.setVisible(true);
    }
    
    public boolean buscarArchivo(String filename){
        boolean aux = false;
        File dir = new File(pathBusc);
        String[] pathnames = dir.list((File dir1, String name1) -> name1.equals(filename));
        if(pathnames.length != 0)
            aux = true;
        return aux;
    }

    public void recibirPeticion(long idoriginal, long idsolicitante, String filename){
        if(Thread.currentThread().getId() != idoriginal){
            mensajes.append("Buscar: " + filename + ".\n Solicitado por hilo: " + idsolicitante);
            if (buscarArchivo(filename)){
                mensajes.append("\nArchivo encontrado en: " + pathBusc);
                ant.recibirRespuesta(idoriginal, filename, pathBusc);
            }
            else{
                mensajes.append("\nArchivo no encontrado. Preguntare al sig.");
                sig.recibirPeticion(idoriginal, Thread.currentThread().getId(), filename);
            }
        }
        else{
            mensajes.append("\nArchivo no encontrado en anillo.");
        }
    }
    
    public void recibirRespuesta(long idoriginal, String filename, String path){
        if(Thread.currentThread().getId() == idoriginal){
            mensajes.append("\nArchivo encontrado en: " + path);
        }
        else{
            mensajes.append("\nArchivo encontrado en: " + path);
            mensajes.append("\nComunicando a solicitante.");
            ant.recibirRespuesta(idoriginal, filename, path);
        }
    }
    
    public void respSencilla(long idoriginal, long idremit, long idSigRemit){
        mensajes.append("\nHilo: " + idremit + " no tuvo exito.\n Le preguntara a: " + idSigRemit);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sauce = (JButton) e.getSource();
        if(sauce == buscar){
            entrada = nombreField.getText();
        }
    }

    @Override
    public void run() {
        vista();
    }
    
    public PantallaBusqueda getAnt() {
        return ant;
    }

    public void setAnt(PantallaBusqueda ant) {
        this.ant = ant;
    }

    public PantallaBusqueda getSig() {
        return sig;
    }

    public void setSig(PantallaBusqueda sig) {
        this.sig = sig;
    }
}
