/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen1redes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author imanol
 */
public class HiloBusqueda extends Thread implements ActionListener{
    
    String path;
    HiloBusqueda hiloAnt, hiloSig;
    PantallaBusqueda view;
    int hilosAct;
    Object syncObject;
    
    
    @Override
    public void run(){
        view = new PantallaBusqueda(hilosAct, Thread.currentThread().getId(), hiloAnt.getId(), hiloSig.getId(), this);
        acceso(Thread.currentThread().getId());
    }
    
    public synchronized boolean buscarArchivo(String filename){
        System.out.println("Buscar " + filename + " en " + path);
        boolean aux = false;
        File dir = new File(path);
        String[] pathnames = dir.list((File dir1, String name1) -> name1.equals(filename));
        if(pathnames.length != 0)
            aux = true;
        return aux;
    }
    
    public synchronized long acceso(long id){
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloBusqueda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    public synchronized void recibePeticion(HiloBusqueda hilo_origen, HiloBusqueda hilo_solicitante, HiloBusqueda hilo_destino, String filename){
        if(hilo_origen.equals(hilo_destino)){
            hilo_origen.getView().getMensajes().append("Anillo recorrido, archivo no encontrado\n");
        }
        else{
            hilo_destino.getView().getMensajes().append("Buscar: " + filename + " sol->" + (getId()) + "\n");
            if(hilo_destino.buscarArchivo(filename)){
                hilo_destino.getView().getMensajes().append("***Archivo encontrado: " + hilo_destino.getPath() + "\n");
                getHiloAnt().devolverRespuesta(hilo_origen, this, hilo_destino, hilo_destino.getPath());
            }
            else{
                hilo_destino.getView().getMensajes().append("No encontrado. Preg a " + hilo_destino.getHiloSig().getId() + "\n");
                hilo_destino.recibePeticion(hilo_origen, this, hilo_destino.getHiloSig(), filename);
            }
        }
    }
    
    public synchronized void devolverRespuesta(HiloBusqueda hilo_origen, HiloBusqueda hilo_anterior, HiloBusqueda hilo_enc, String path){
        if(hilo_origen.equals(hilo_anterior)){
            hilo_origen.getView().getMensajes().append("Anillo recorrido.\n>Encontrado por " + hilo_enc.getId() + " en: " + path + "\n");
        }
        else{
            hilo_anterior.getView().getMensajes().append("--> Encontrado por " + hilo_enc.getId() + "\nEn: " + path);
            hilo_anterior.devolverRespuesta(hilo_origen, hilo_anterior.getHiloAnt(), hilo_enc, path);
        }
    }
    
    /*Metodo para testear ubicacion de creacion*/
    public void crearDirectorio() throws IOException{
        File directorio = new File("./busqueda/hola");
        if (!directorio.exists()) {
            Files.createDirectory(directorio.toPath());
        }
    }

    @Override
    public synchronized void actionPerformed(ActionEvent ae) {
        String filename = view.getNombreField().getText();
        if(buscarArchivo(filename)){
            view.getMensajes().append("Encontre el archivo en" + path + "\n");
        }
        else{
            view.getMensajes().append("No encontrado. Llamando->"+hiloSig.getId() + "\n");
            recibePeticion(this, this, this.getHiloSig(), filename);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setHilosAct(int hilosAct) {
        this.hilosAct = hilosAct;
    }

    public HiloBusqueda getHiloAnt() {
        return hiloAnt;
    }

    public void setHiloAnt(HiloBusqueda hiloAnt) {
        this.hiloAnt = hiloAnt;
    }

    public HiloBusqueda getHiloSig() {
        return hiloSig;
    }

    public void setHiloSig(HiloBusqueda hiloSig) {
        this.hiloSig = hiloSig;
    }

    public PantallaBusqueda getView() {
        return view;
    }
}
