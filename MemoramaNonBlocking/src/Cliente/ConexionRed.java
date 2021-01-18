/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author imanol
 */
public class ConexionRed implements Runnable{
    
    private final String LOCAL_HOST = "127.0.0.1";
    private final int PORT = 12000;
    private final int MAX_MESSAGE_SIZE = 40000;
    private SocketChannel server;
    private InetSocketAddress serverAddress;
    //private OutputHandler outputHandler;
    private Selector selector;
    private boolean connected = false;
    private boolean timeToSendMessage = false;
    private final Queue<String> messagesToServer = new LinkedList<>();
    private ByteBuffer buffer;
    int cont = 0;
    
    public ConexionRed(){
        buffer = ByteBuffer.allocate(MAX_MESSAGE_SIZE);
    }

    @Override
    public void run() {
        initConnect();
        try {
            initSelector();
            //getGameImgs(server);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConexionRed.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while (connected) {
            if (timeToSendMessage) {
                server.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                timeToSendMessage = false;
            }
            try {
                selector.select();
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isReadable()) {
                        recvMessageFromServer3(key);
                        //getGameImgs((SocketChannel) key.channel());
                        //recvMessageFromServer2(key);
                    } else if (key.isWritable()) {
                        sendMessageToServer(key);
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not selector.select in server connection thread " + e.getMessage());
            }
        }
    }
    
    
    public void connect() {
        serverAddress = new InetSocketAddress(LOCAL_HOST, PORT);
        new Thread(this).start();
        //this.outputHandler = outputHandler;
    }
    
    private void initSelector() throws InterruptedException {
        try {
            selector = Selector.open();
            //recvMessageFromServer3(server);
            //recvMessageFromServerFinal(server);
            server.register(selector, SelectionKey.OP_READ);
            
        } catch (IOException ioe) {
            System.err.println("Could not init selector " + ioe.getMessage());
        }
    }

    private void initConnect() {
        try {
            server = SocketChannel.open(serverAddress);
            server.configureBlocking(false);
            connected = true;
        } catch (IOException ioe) {
            System.err.println("Could not init connect " + ioe.getMessage());
        }
    }

    public void disconnect() {
        try {
            connected = false;
            server.close();
        } catch (IOException ioe) {
            System.err.println("Could not disconnect " + ioe.getMessage());
        }
    }

    private void recvMessageFromServer(SelectionKey key) {
        buffer.clear();
        SocketChannel server = (SocketChannel) key.channel();
        try {
            server.read(buffer);
            String msg = bytebufferToString();
            //outputHandler.printToConsole(msg);
            System.out.println(msg);
            buffer.clear();
        } catch (IOException ioe) {
            System.err.println("Error in recvMessageFromServer " + ioe.getMessage());
            System.exit(1);
        }
        buffer.clear();
    }

    public void addMessageToSend(String message) {
        synchronized (messagesToServer) {
            messagesToServer.add(message);
        }
        timeToSendMessage = true;
        selector.wakeup();
    }

    private void sendMessageToServer(SelectionKey key) {
        buffer.clear();
        String msg;
        synchronized (messagesToServer) {
            while ((msg = messagesToServer.peek()) != null) {
                buffer.put(msg.getBytes());
                messagesToServer.remove();
            }
        }
        buffer.flip();
        try {
            while (buffer.hasRemaining()) {
                server.write(buffer);
            }
        } catch (IOException ioe) {
            System.err.println("IOException " + ioe.getMessage());
        }
        buffer.clear();
        key.interestOps(SelectionKey.OP_READ);
    }

    private String bytebufferToString() {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes);
    }
    
   
    private void recvMessageFromServer3(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel)key.channel();
        String out_filename = "./src/Cliente/imagen" + cont + ".jpg";
        int bufferSize = 102400;
        Path path = Paths.get(out_filename);
        FileChannel fc = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)
                );
        ByteBuffer bb = ByteBuffer.allocate(bufferSize);
        int res = 0;
        int conta = 1;
        do{
            bb.clear();
            if((res = channel.read(bb)) == 0)
                break;
            System.out.println(res);
            bb.flip();
            if(res > 0){
                fc.write(bb);
                conta += res;
            }
        } while(res >= 0);
        fc.close();
        System.out.println("Received: " + conta);
        cont++;
        key.interestOps(SelectionKey.OP_READ);
    }
    
    private void recvMessageFromServerFinal(SocketChannel channel) throws IOException{
        String ppath = "./src/Cliente/img/";
        int bufferSize = 102400;
        String fname = "";
        String conf = "false";
        ByteBuffer bb = ByteBuffer.allocate(bufferSize);
        
        
        int res = 0; 
        int cont = 1;
        
        for(int i = 0; i < 21; i++){
            fname = "imagen" + i + ".jpg";
            Path path = Paths.get(ppath+fname);
            System.out.println("Recibiendo "+ ppath+fname);
            FileChannel fc = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)
                );
            do {
                bb.clear();
                if ((res = channel.read(bb)) == 0) {
                    break;
                }
                System.out.println(res);
                bb.flip();
                if (res > 0) {
                    fc.write(bb);
                    cont += res;
                }
            } while (res >= 0);
            System.out.println("Received: " + cont);
            
            conf = "true";
            ByteBuffer msjConf = ByteBuffer.wrap(conf.getBytes());
            channel.write(msjConf);
            msjConf.clear();
            conf = "false";

            res = 0;
            cont = 1;
            fc.close();
            bb.clear();
        }
        System.out.println("Todas las imagenes han sido recibidas");
    }
}
