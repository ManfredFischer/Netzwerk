/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class DateiVerwaltung {

    public int Port() {
        int Port = 8080;
        Boolean weiter = false;
        ServerSocket s;
        while (Port == 8080 || weiter) {
            Port = (int) (Math.random() * 65535 + 1);
            try {
                s = new ServerSocket(Port);
                weiter = false;
                s.close();
            } catch (IOException ex) {
                weiter = true;
            }
        }
        return Port;
    }

    public void dateiVersand(File datei, String IP, int Port) {
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(IP, Port));
            Socket VergindungsSocket = socketChannel.socket();
            FileInputStream fileInputStream = new FileInputStream(datei);
            DataOutputStream dataOutputStream = new DataOutputStream(VergindungsSocket.getOutputStream());
            dataOutputStream.writeLong(datei.length());
            dataOutputStream.flush();
            FileChannel fileChannel = fileInputStream.getChannel();
            transfer(fileChannel, socketChannel, datei.length(), 1024 * 1024 * 16, true, true);
            fileInputStream.close();
            VergindungsSocket.close();
            socketChannel.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public long zwischenWert = 0, Gesamtwert=0;
    
    
    
    public ArrayList<File> dateiEmpfang(int Port, String pfad, String DateiName, VerwaltungDatenverarbeitung nt) {
        String fileName = "", dest = "";
        ArrayList<File> files = new ArrayList<File>();
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(Port));
            SocketChannel socketChannel = serverSocketChannel.accept();
            Socket socket = socketChannel.socket();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            long sizeInBytes = dataInputStream.readLong();
            String dateiNT = dataInputStream.readUTF();
            if (!dateiNT.equals("FEHLER")){
            File file2 = new File("Daten/" + pfad + "/");
            file2.mkdir();
            File file = new File("Daten/" + pfad + "/" + DateiName);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            FileChannel fileChannel = fileOutputStream.getChannel();

            transfer(fileChannel, socketChannel, sizeInBytes, 1024 * 1024 * 16, true, false);

            fileOutputStream.close();
            socket.close();
            serverSocketChannel.close();
            if (DateiName.endsWith(".zip")) {
                ZipFile zip = new ZipFile();
                files = zip.getZipFiles("Daten/" + pfad + "/" + DateiName, "Daten/" + pfad + "/");
                if (files.isEmpty()) {
                    files.add(new File("Temp/x-x-Stoerung.txt"));
                }
                file.delete();
            } else {
                files.add(file);
            }
            }else{
              files = new ArrayList<File>();  
            }


        } catch (Exception ex) {
            Gesamtwert = -1;
        }
        nt.uebertragungFertig = true;
        return files;
    }

    public void transfer(FileChannel fileChannel, SocketChannel socketChannel, long lengthInBytes, long chunckSizeInBytes, boolean verbose, boolean fromFileToSocket)
            throws IOException {

        long overallBytesTransfered = 0L;
        long time = -System.currentTimeMillis();
        while (overallBytesTransfered < lengthInBytes) {
            Gesamtwert = lengthInBytes;
            zwischenWert = overallBytesTransfered;
            long bytesTransfered = 0L;

            if (fromFileToSocket) {
                bytesTransfered = fileChannel.transferTo(overallBytesTransfered, Math.min(
                        chunckSizeInBytes, lengthInBytes
                        - overallBytesTransfered), socketChannel);
            } else {
                bytesTransfered = fileChannel.transferFrom(socketChannel,
                        overallBytesTransfered, Math.min(chunckSizeInBytes,
                        lengthInBytes - overallBytesTransfered));
            }

            overallBytesTransfered += bytesTransfered;

            if (verbose) {
            }

        }
        time += System.currentTimeMillis();

        if (verbose) {
        }

    }
}
