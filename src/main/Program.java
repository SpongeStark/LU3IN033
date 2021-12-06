package main;

import controller.MainActivity;
import model.Ethernet;
import model.Frame;
import utility.MyFileReader;
import utility.Tools;
import view.MainWindow;

import javax.swing.*;

public class Program {
    public static void main(String[] args) {
        System.out.println("Hello World");
//        MainActivity mainActivity = new MainActivity();
//        mainActivity.start();
//        String path = "/Users/yangkai/Desktop/package.txt";
//        MyFileReader myReader = new MyFileReader(path);
//        System.out.println(myReader);

        String path = "/Users/yangkai/Desktop/stream.txt";
        String codes = (new MyFileReader(path)).toString().replace("\n","");
        Ethernet eth = new Ethernet(codes.toCharArray());
        System.out.println(eth);
//        char[] code = codes.toCharArray();
//        System.out.println(code[4]);

    }
}