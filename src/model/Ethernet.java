package model;

import exception.ConvertException;
import utility.Tools;

import java.util.Arrays;

public class Ethernet implements Message {
    private String[] destination; // Mac destination
    private String[] source; // Mac source
    private String type; // Ethertype (Ethernet II)
    Message data;
    private boolean isValid;
    private String log;

    public Ethernet(char[] code) {
        this.log = "";
        this.decode(code);
    }

    // region Decode
    public void decode(char[] code){
        destination = decodeMACAddress(Arrays.copyOfRange(code, 0, 12));
        source = decodeMACAddress(Arrays.copyOfRange(code, 12, 12+12));
        type = String.copyValueOf(code, 24, 4);
        if(type.equals("0800")){
            data = new IP(Arrays.copyOfRange(code, 28, code.length));
        }else{
            data = new Data(Arrays.copyOfRange(code, 28, code.length));
            log += "Do not recognize the type.\n";
        }
    }

    private String[] decodeMACAddress(char[] MAC){
        String[] res = new String[6];
        for(int i=0; i<6; i++){
//            res[i] = String.valueOf(Arrays.copyOfRange(MAC, 2*i, 2*i+2));
            res[i] = String.copyValueOf(MAC, 2*i, 2);
        }
        return res;
    }
    // endregion

    // region Display
    private String getDestToString(){
        StringBuilder res = new StringBuilder();
        try{
            res.append("Destination MAC Address (").append(destination[0]);
            for(int i=1; i<6; i++){
                res.append(":").append(destination[i]);
            }
            res.append(") : ").append(Tools.hex2dec(destination[0]));
            for(int i=1; i<6; i++){
                res.append(":").append(Tools.hex2dec(destination[i]));
            }
            res.append("\n");
            return res.toString();
        }catch (ConvertException e){
            isValid = false;
            log += "Destination address error.\n";
            return "Destination MAC Address : ERROR\n";
        }
    }

    private String getSrcToString(){
        StringBuilder res = new StringBuilder();
        try{
            res.append("Source MAC Address (").append(source[0]);
            for(int i=1; i<6; i++){
                res.append(":").append(source[i]);
            }
            res.append(") : ").append(Tools.hex2dec(source[0]));
            for(int i=1; i<6; i++){
                res.append(":").append(Tools.hex2dec(source[i]));
            }
            res.append("\n");
            return res.toString();
        }catch (ConvertException e){
            isValid = false;
            log += "Source address error.\n";
            return "Source MAC Address : ERROR\n";
        }
    }

    private String getTypeToString(){
        StringBuilder res = new StringBuilder("Type (0x");
        res.append(this.type).append(") : ");
        if(this.type.equals("0800")){
            res.append("IP\n");
        }else{
            res.append("Do not recognize the type\n");
        }
        return res.toString();
    }
    // endregion


    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append("Ethernet : \n");
        res.append(getDestToString());
        res.append(getSrcToString());
        res.append(getTypeToString());
        res.append("--------------------\n");
        res.append(data.toString());
        return res.toString();
    }
}
