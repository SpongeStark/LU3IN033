package main;

import controller.MainActivity;
import model.Ethernet;
import model.Frame;
import utility.MyFileReader;

public class Program {
    public static void main(String[] args) {
        System.out.println("Hello World");
        test();
//        test2();
    }

    public static void test(){
        MainActivity mainActivity = new MainActivity();
        mainActivity.start();
//        String path = "/Users/yangkai/Desktop/package.txt";
//        MyFileReader myReader = new MyFileReader(path);
//        System.out.println(myReader);

//        String path = "/Users/yangkai/Desktop/stream.txt";
//        String codes = (new MyFileReader(path)).toString().replace("\n","");
//        String path = "/Users/yangkai/Desktop/package.txt";
//        MyFileReader file = new MyFileReader(path);
//        if(file.readFile()){
//            try{
//                Frame[] frames = file.getFrames();
//                for(Frame item : frames){
//                    System.out.println(item);
//                }
//            }catch (Exception e){
//                System.out.println("decoding - Error");
//            }
//        }else{
//            System.out.println("File reading - Error");
//        }


    }

    public static void test2(){
        String path = "C:/Users/lenovo/Desktop/stream.txt";
        String codes = (new MyFileReader(path)).getFrames()[0].toString();
        System.out.println(codes);
        codes = (new MyFileReader(path)).getFrames()[1].toString();
        System.out.println(codes);
    }
}