/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author imanol
 */
public class ServerMain {
    public static void main(String[] args) {
        new Server().runServer();
    }
}

class Server{
    
    private static final String LOCAL_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 12000;
    private final int MAX_MESSAGE_SIZE = 40000;
    private ServerSocketChannel server;
    private Selector selector;
    private final ByteBuffer buffer;
    private String responseMessage;
    private Object respObj;
    
    public Server() {
        this.buffer = ByteBuffer.allocate(MAX_MESSAGE_SIZE);
    }
    
    public void runServer(){
        initSelector();
        initServer();
        while(true){
            try{
                selector.select();
            } catch(IOException e){
                System.err.println("Error: selector en runServer()" + e.getMessage());
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while(it.hasNext()){
                SelectionKey key = it.next();
                it.remove();
                if(!key.isValid())
                    continue;
                if(key.isAcceptable()){
                    accept(key);
                } else if(key.isReadable()){
                    recvMessageFromClient(key);
                } else if(key.isWritable()){
                    sendMessageToClient(key);
                }
            }
        }
    }
    
    private void initSelector() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            System.err.println("Error en initSelector() " + e.getMessage());
        }
    }

    private void initServer() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(LOCAL_HOST, DEFAULT_PORT));
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.err.println("Error al iniciar el server initServer() " + e.getMessage());
        }
        print("Server funcionando...");
    }
    
    private void accept(SelectionKey key){
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel client = serverSocketChannel.accept();
            print("Conexion con " + client.getLocalAddress());
            client.configureBlocking(false);
            
            //SelectionKey clientKey = client.register(selector, SelectionKey.OP_WRITE);
            sendGameImgsFinal(client);
            //clientKey.attach(cont.initGame());
            responseMessage = "hola";//cont.startGame((Game) clientKey.attachment());
            selector.wakeup();
        } catch (IOException ioe) {
            System.err.println("Excepcion lanzada en accept() " + ioe.getMessage());
        }
    }

    private void sendMessageToClient(SelectionKey key) {
        buffer.clear();
        SocketChannel client = (SocketChannel) key.channel();
        buffer.put(responseMessage.getBytes());
        buffer.flip();
        try {
            while (buffer.hasRemaining()) {
                client.write(buffer);
            }
        } catch (IOException ioe) {
            System.err.println("Problema del buffer en sendMessageToClient " + ioe.getMessage());
        }
        client.keyFor(selector).interestOps(SelectionKey.OP_READ);
        buffer.clear();
    }

    private void recvMessageFromClient(SelectionKey key) {
        buffer.clear();
        SocketChannel client = (SocketChannel) key.channel();
        try {
            client.read(buffer);
            String clientMessage = bytebufferToString();
            //responseMessage = cont.progress(clientMessage.toLowerCase(), (Game) key.attachment());
            client.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
        } catch (IOException ioe) {
            try {
                System.err.println("Cliente desconectado" + client.getLocalAddress());
                client.close();
            } catch (IOException e) {
                System.err.println("No fue posible cerrar la conexion " + e.getMessage());
            }
        }
        buffer.clear();
    }
    
    private void print(String msg) {
        System.out.println(msg);
    }
    
    public static Object byteBufferToObject(ByteBuffer byteBuffer)
            throws Exception {
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        Object object = deSerializer(bytes);
        return object;
    }
    
    private String bytebufferToString() {
        buffer.flip();
        byte [] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes);
    }

    public static Object deSerializer(byte[] bytes) throws IOException,
            ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new ByteArrayInputStream(bytes));
        return objectInputStream.readObject();
    }
    
    public void sendGameImgs(SocketChannel cliente){
        File file = new File("./src/Servidor/imgs/fondo.jpg");
        String filename = file.getName();
        byte[] namebytes = filename.getBytes();
        ByteBuffer nameBuffer = ByteBuffer.wrap(namebytes);
        
        try {
            cliente.write(nameBuffer);
            FileChannel sbc = FileChannel.open(file.toPath());
            ByteBuffer fileBuffer = ByteBuffer.allocate(1024);
            int bytesread = sbc.read(fileBuffer);
            while(bytesread != -1){
                fileBuffer.flip();
                cliente.write(fileBuffer);
                fileBuffer.compact();
                bytesread = sbc.read(fileBuffer);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendGameImgs2(SocketChannel cliente) throws IOException{
        String fname = "./src/Servidor/imgs/fondo.jpg";
        int bufferSize = 10240;
        Path path = Paths.get(fname);
        FileChannel fc = FileChannel.open(path);
        ByteBuffer bb = ByteBuffer.allocate(bufferSize);
        int numBytes = 0;
        int cont = 0;
        do{
            numBytes = fc.read(bb);
            if(numBytes <= 0)
                break;
            cont += numBytes;
            bb.flip();
            do{
                numBytes -= cliente.write(bb);
            }while(numBytes > 0);
            bb.clear();
        }while(true);
        fc.close();
        System.out.println("Sended: " + cont);
    }
    
    public void sendGameImgsFinal(SocketChannel cliente) throws IOException{
        String ppath = "./src/Servidor/imgs/";
        String fname;
        //String confirm = "";
        int bufferSize = 102400;
        int numBytes = 0;
        int cont = 0;
        
        ByteBuffer bb = ByteBuffer.allocate(bufferSize);
        ByteBuffer msjConf = ByteBuffer.allocate(10);
        msjConf.clear();
        
        for(int i = 0; i < 21; i++){
            fname = "imagen" + i + ".jpg";
            Path path = Paths.get(ppath + fname);
            System.out.println("Enviado " + ppath + fname);
            FileChannel fc = FileChannel.open(path);
            do {
                numBytes = fc.read(bb);
                System.out.println(numBytes);
                if (numBytes <= 0) {
                    break;
                }
                cont += numBytes;
                bb.flip();
                do {
                    numBytes -= cliente.write(bb);
                } while (numBytes > 0);
                bb.clear();
            } while (true);
            /*while(!confirm.equals("true")){
                int n = cliente.read(msjConf);
                confirm = new String(msjConf.array(), 0, n);
                System.out.println(confirm);
            }
            confirm = "";*/
            fc.close();
            System.out.println("Sended: " + cont);
            cont = 0;
        }
    }
}
