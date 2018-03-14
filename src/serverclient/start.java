/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author selem
 */
public class start {

    public static void main(String[] args) throws IOException, InterruptedException {

        Properties prop = new Properties();
        InputStream input = null;
        int numofwriters;
        int numofreaders;
        int numofaccesses;

        try {

            input = new FileInputStream("system.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            numofwriters = Integer.parseInt(prop.getProperty("RW.numberOfWriters"));
            numofreaders = Integer.parseInt(prop.getProperty("RW.numberOfReaders"));
            numofaccesses = Integer.parseInt(prop.getProperty("RW.numberOfAccesses"));
            Thread[] readers = new Thread[numofreaders];
            Thread[] writers = new Thread[numofwriters];

            Thread server = new Server();

            server.start();
            for (int i = 0; i < numofreaders; i++) {
                readers[i] = new Reader(numofaccesses, i);

            }
            for (int j = 0; j < numofwriters; j++) {
                writers[j] = new Writer(numofaccesses, j + numofreaders);

            }

            for (int i = 0; i < numofreaders; i++) {

                readers[i].start();
                

            }
            
            for (int i = 0; i < numofwriters; i++) {

                
                writers[i].start();

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
