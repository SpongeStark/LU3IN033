package model;

public class Data implements Message{

    char[] data;

    public Data(){}

    public Data(char[] code){
        decode(code);
    }

    public void decode(char[] codes){
        this.data = codes;
    }

    public String toString(){
        return "Data :\n"+String.valueOf(data)+"\n";
    }
}
