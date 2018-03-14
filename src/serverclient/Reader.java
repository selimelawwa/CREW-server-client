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
import java.net.*;
import java.util.Random;
import java.util.Scanner;

// Client class
public class Reader extends Thread {

    public int numofaccesses;
    public int rid;
    public int counter = 0;

    public Reader(int numofaccesess, int id) {
        this.numofaccesses = numofaccesess;
        this.rid = id;
    }

    @Override
    public void run() {
        try {
            Scanner scn = new Scanner(System.in);

            InetAddress ip = InetAddress.getByName("localhost");

            Socket s = new Socket(ip, 5056);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            File file = new File("log" + rid + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Type: Reader \n");
            bw.write("Client Name:" + rid + "\n");
            bw.write("\n \n sSeq   oVal \n ");

            while (counter < numofaccesses) {
                String idstring = Integer.toString(rid);
                String tosend = "GET," + idstring;
                dos.writeUTF(tosend);
//                int tempSseq  = Server.sseq + 1;
//                Server.sseq=tempSseq;

                String received = dis.readUTF();
                String[] ss = received.split(",");
                String tempSseq = ss[1];
                String updatedOval = ss[0];
                System.out.println("iteration #  " + counter + " reader " + rid + " Oval: " + updatedOval + " seq " + tempSseq);
                
                
                bw.write("\n" +" " +tempSseq + "   " + updatedOval + "\n");
                

                counter++;

            }
            bw.close();

            String tosend = "Exit";
            dos.writeUTF(tosend);
            s.close();
            System.out.println("Connection closed with reader:" + rid);

            scn.close();
            dis.close();
            dos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
