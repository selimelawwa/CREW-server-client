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
public class Writer extends Thread {

    public int numofaccesses;
    public int counter = 0;
    public int wid;

    public Writer(int numofaccesess, int id) {
        this.numofaccesses = numofaccesess;
        this.wid = id;
    }

    @Override
    public void run() {
        try {
            Scanner scn = new Scanner(System.in);

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            File file = new File("log" + wid + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Type: Reader \n");
            bw.write("Client Name:" + wid + "\n");
            bw.write("\n \n sSeq   oVal \n ");

            while (counter < numofaccesses) {
                Random rn2 = new Random();
                int newval2 = rn2.nextInt(100) + 1;
                String newvalstring = Integer.toString(newval2);
                String idstring = Integer.toString(wid);
                String tosend = "SET," + idstring + "," + newvalstring;
                dos.writeUTF(tosend);

                String received = dis.readUTF();

                if (!received.equals("wait")) {

//                    int tempSseq = Server.sseq + 1;
//                    Server.sseq = tempSseq;
                    String[] ss = received.split(",");
                    String tempSseq = ss[1];

                    System.out.println("iteration #" + counter + " writer " + wid + " Oval: " + ss[0] + " seq " + tempSseq);
                    counter++;

                    bw.write("\n" + " " + tempSseq + "   " + ss[0] + "\n");
                    //bw.close();

                } else {
                    System.out.println("writer: " + wid + " has to wait bec of other writer");
                }

            }

            bw.close();
            String tosend = "Exit";
            dos.writeUTF(tosend);

            s.close();
            System.out.println("Connection closed with writer: " + wid);

            // closing resources
            scn.close();
            dis.close();
            dos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
