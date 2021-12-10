package main;

import controller.MainActivity;
import exception.ConvertException;
import model.DNS;
import utility.MyFileReader;

import java.util.ArrayList;

public class Program {
    public static void main(String[] args) {
        MainActivity mainActivity = new MainActivity();
        mainActivity.start();
//        String url = "03 77 77 77 07 79 6f 75 74 75 62 65 03 63 6f 6d 00 00 01 00 01 03 63 6f 6d c004000500";
//        char[] cs = trimArray(url);
////        System.out.println(cs[43]);
//        try {
//            System.out.println(DNS.decodeDomainName(cs, 42));
//        } catch (ConvertException e) {
//            e.printStackTrace();
//        }
    }

    public static char[] trimArray(String array){
        StringBuilder res = new StringBuilder();
        for(char s : array.toCharArray()){
            if(s!=' '){
                res.append(String.valueOf(s));
            }
        }
        return res.toString().toCharArray();
    }
}