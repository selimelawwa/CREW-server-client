/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

import java.util.Random;

/**
 *
 * @author selem
 */
public class test {

    public static void main(String[] args) {
//        String str = "abc,selim";
//        String[] ss = str.split(",");
//        System.out.println(ss[1]);

//        Random rn = new Random();
//        int newval = rn.nextInt(100) + 1 ;
//        String newvalstring = Integer.toString(newval);
//        String text = "SET," + newvalstring;
//        System.out.println(text);
        String[] ary = "SET,47".split("");
        System.out.println(ary[0] + ary[1] + ary[2]);
        String my = ary[0] + ary[1] + ary[2];
        if (my.equals("SET")) {
            System.out.println("Selim is amazing");
        }
//        

    }
}
