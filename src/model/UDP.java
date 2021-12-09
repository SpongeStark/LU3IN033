package model;

import exception.ConvertException;
import utility.Tools;

import java.util.Arrays;

public class UDP implements Message{
    private String srcPort;
    private String destPort;
    private String length;
    private String checksum;
    private String pseudoHeader;
    private Message data;
    private String log;
    private boolean isChecksumValid;

    public UDP(){
        this.log = "";
        this.isChecksumValid = false;
    }

    public UDP(char[] codes){
        this.log = "";
        decode(codes);
    }

    // region Decode
    public void decode(char[] codes){
        this.srcPort = String.valueOf(codes, 0, 4);
        this.destPort = String.valueOf(codes, 4, 4);
        this.length = String.valueOf(codes, 8, 4);
        // all zeros 0x00 and UDP 0x11
        this.pseudoHeader += ("0011"+this.length);
        this.checksum = String.valueOf(codes, 12, 4);
        this.isChecksumValid = this.verifyChecksum(codes);
        if(this.destPort.equals("0035") || this.srcPort.equals("0035")){ // DNS
            this.data = new DNS(Arrays.copyOfRange(codes, 16, codes.length));
        }else if(this.destPort.equals("0043")||this.destPort.equals("0044")) {
            this.data = new DHCP(Arrays.copyOfRange(codes, 16, codes.length));
        }else{
            this.data = new Data(Arrays.copyOfRange(codes, 16, codes.length));
        }
    }

    // endregion

    public void setPseudoHeader(String[] srcAddr, String[] destAddr){
        StringBuilder res = new StringBuilder();
        for(String s : srcAddr){
            res.append(s);
        }
        for(String s : destAddr){
            res.append(s);
        }
        pseudoHeader = res.toString();
    }

    private int getPseudoHeaderSum(){
        try{
            int len = 6;
            int sum = 0;
            for(int i=0; i<len; i++){
                sum += Tools.hex2dec(this.pseudoHeader.substring(i*4, i*4+4));
            }
            return sum;
        }catch (ConvertException e){
            return 0;
        }
    }

    boolean verifyChecksum(char[] codes){
        try{
            StringBuilder str_codes = new StringBuilder(String.valueOf(codes));
            int miss = 4 - str_codes.length()%4;
            if(miss != 4){
                for(int i=0; i<miss; i++){ str_codes.append("0"); }
            }
            int len = str_codes.length()/4;
            int sum = getPseudoHeaderSum();
            for(int i=0; i<len; i++){
                sum += Tools.hex2dec(str_codes.substring(4*i, 4*i+4));
            }
            int boundary = 0xffff + 1;
            while(sum > 0xffff){
                sum = sum/boundary + sum%boundary;
            }
            return sum==0xffff;
        }catch (ConvertException e){
            return false;
        }
    }

    // region Display

    private String getValueToString(String title, String hexValue){
        StringBuilder res = new StringBuilder(title);
        res.append(" (0x").append(hexValue).append(") : ");
        try {
            res.append(Tools.hex2dec(hexValue)).append("\n");
        } catch (ConvertException e) {
            res.append("ERROR\n");
            this.log += (title + " - Error\n");
        }
        return res.toString();
    }

    String getSrcPortToString(){
        return getValueToString("Source port", this.srcPort);
    }

    String getDestPortToString(){
        return getValueToString("Destination port", this.destPort);
    }

    String getLengthToString(){
        return getValueToString("Length", this.length);
    }

    private String getCheckSumToString(){
        StringBuilder res = new StringBuilder("Check Sum : ");
        res.append("0x").append(this.checksum);
        if(isChecksumValid){
            res.append(" [Valid]\n");
        }else{
            res.append(" [non valid]\n");
        }
        return res.toString();
    }

    // endregion

    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append("User Datagram Protocol : \n");
        res.append(this.getSrcPortToString());
        res.append(this.getDestPortToString());
        res.append(this.getLengthToString());
        res.append(this.getCheckSumToString());
        res.append("--------------------\n");
        res.append(data.toString());
        return res.toString();
    }

}
