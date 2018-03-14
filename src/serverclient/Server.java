/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

/**
 *
 * @author selem
 */
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// Server class
public class Server extends Thread {

    public static int sseq = 0;
    public static int seq = 0;
    public static int Oval = 0;
    public static boolean otherwriter = false;
    public static int rNum = 0;

    @Override
    public void run() {
        try {
            System.out.println("Server Started");

            ServerSocket ss = new ServerSocket(5056);

            while (true) {
                Socket s = null;

                try {

                    s = ss.accept();

                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    Thread t = new ClientHandler(s, dis, dos);

                    t.start();

                } catch (Exception e) {
                    s.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {

        String received;
        String toreturn;

        while (true) {

            try {

                received = dis.readUTF();
                String[] ary = received.split("");
                String receivedtemp = ary[0] + ary[1] + ary[2];

                if (received.equals("Exit")) {
                    this.s.close();
                    break;
                }

                if (receivedtemp.equals("GET")) {
                    String[] ss = received.split(",");
                    //String senderid = ss[1];
                    int temprid = Integer.parseInt(ss[1]);
                    Server.rNum += 1;
                    
                    Server.sseq = Server.sseq+1;
                    int tempsseq = Server.sseq;
                                       
                    toreturn = Integer.toString(Server.Oval) + "," + Integer.toString(Server.sseq) ;
                    dos.writeUTF(toreturn);
                    //System.out.println("sSeq: " + Server.sseq + "oVal: " + Server.Oval+  "rID: " + temprid + "rNum: " + Server.rNum);


                    File file = new File("Server-Readers.txt");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("\n" +tempsseq+ "   " + Server.Oval + "   " + temprid + "   " + Server.rNum + "\n");
                    bw.close();

                    for (int i = 0; i < 9999; i++) {
                        for (int j = 0; j < 9999; j++) {
                            int x = 1;
                            int y = x * 2;
                        }
                    }

                    Server.rNum -= 1;

                } else if (receivedtemp.equals("SET") && Server.otherwriter == false) {
                    Server.otherwriter = true;
                    String[] ss = received.split(",");
                    //String senderid = ss[1];
                    int tempwid = Integer.parseInt(ss[1]);
                    //String temp = ss[2];
                    //int newval = Integer.parseInt(ss[2]);
                    Server.Oval = Integer.parseInt(ss[2]);
                    
                    Server.sseq = Server.sseq + 1;
                    int tempsseq = Server.sseq;
                                       
                    toreturn = Integer.toString(Server.Oval) + "," + Integer.toString(Server.sseq) ;
                    
                    //String send = "Oval updated value is::" + Integer.toString(newval);
                    dos.writeUTF(toreturn);
                    
                    File file = new File("Server-Writers.txt");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("\n" +tempsseq + "   " + Integer.parseInt(ss[2]) + "   " + tempwid + "   " + "\n");
                    bw.close();
                    
                    
                    //System.out.println("sSeq: " + Server.sseq + "oVal: " + Server.Oval+  "wID: " + tempwid);
                    for (int i = 0; i < 9999; i++) {
                        for (int j = 0; j < 99999; j++) {
                            int x = 1;
                            int y = x * 2;
                        }
                    }

                    Server.otherwriter = false;
                } else if (receivedtemp.equals("SET") && Server.otherwriter == true) {
                    String send = "wait";
                    dos.writeUTF(send);
                }

                Random rn = new Random();
                int newval2 = rn.nextInt(15) + 2;
                int sleeptime = newval2 * 1000;
                Thread.sleep(sleeptime);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
