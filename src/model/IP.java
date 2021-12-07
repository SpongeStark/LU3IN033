package model;

import exception.ConvertException;
import utility.Tools;

import java.util.Arrays;

public class IP implements Message {

    private char version;
    private char IHL; // internet header length
    private String tos; // Type of service
    private String totLen; // total length (Byte)
    private String id; // identifier
    private boolean[] flags; // NULL, DF, MF
    private String offset;
    private String TTL; // Time to live
    private String protocol; // Protocol
    private String str_protocol;
    private String checkSum; // Check sum
    private String[] src; // Source address
    private String[] dest; // Destination address
    private String option;
    private Message data;
    private String log;
    private final boolean isChecksumValid;


    public IP(char[] code){
        this.log = "";
        this.decode(code);
        this.isChecksumValid = verifyCheckSum(code);
    }

    // region Decode
    public void decode(char[] code){
        this.version = code[0];
        this.IHL = code[1];
        this.tos = String.valueOf(code, 2, 2);
        this.totLen = String.valueOf(code, 4, 4);
        this.id = String.valueOf(code, 8, 4);
        this.flags = decodeFlags(code[12]);
        this.offset = decodeOffset(Arrays.copyOfRange(code, 12, 12+4));
        this.TTL = String.valueOf(code, 16, 2);
        this.setProtocol(String.valueOf(code, 18, 2));
        this.checkSum = String.valueOf(code, 20, 4);
        this.src = decodeIpAddress(String.valueOf(code, 24, 8));
        this.dest = decodeIpAddress(String.valueOf(code, 32, 8));
        if(this.data instanceof UDP){
            ((UDP) this.data).setPseudoHeader(this.src, this.dest);
        }
        try{
            int IHL_value = Tools.hexChar2hex(this.IHL);
            if(!(IHL_value>5)){
                throw new Exception();
            }else{
                this.option = String.valueOf(code, 40, IHL_value*4-40);
            }
            this.data.decode(Arrays.copyOfRange(code, IHL_value, code.length));
        }catch (Exception e){
            this.option = "";
            this.data.decode(Arrays.copyOfRange(code, 40, code.length));
        }
    }

    private boolean[] decodeFlags(char code){
        boolean[] res = new boolean[3];
        try{
            int value = Tools.hexChar2hex(code);
            // 得到前3位
            value /= 2;
            for(int i=2; i>=0; i--){
                res[i] = value % 2 == 1;
                value /= 2;
            }
        }catch (ConvertException e){
            this.log += "Can not decode flags\n";
        }
        return res;
    }

    private String decodeOffset(char[] code){
        StringBuilder res = new StringBuilder();
        try{
            res.append(Tools.hexChar2hex(code[0]) % 2);
            res.append(code[1]).append(code[2]).append(code[3]);
        }catch (ConvertException e){
            this.log += "Fragment offset error\n";
        }
        return res.toString();
    }

    private void setProtocol(String proto){
        this.protocol = proto;
        switch (proto){
            case "01": //1
                this.str_protocol = "ICMP";
                this.data = new Data();
                break;
            case "02": //2
                this.str_protocol = "IGMP";
                this.data = new Data();
                break;
            case "06": //6
                this.str_protocol = "TCP";
                this.data = new Data();
                break;
            case "11": //17
                this.str_protocol = "UDP";
                this.data = new UDP();
                break;
            case "59": //89
                this.str_protocol = "OSPF";
                this.data = new Data();
                break;
            case "84": //132
                this.str_protocol = "SCTP";
                this.data = new Data();
                break;
            default:
                this.str_protocol = "ERROR";
                this.data = new Data();
                this.log += "Protocol - Error\n";
        }
    }

    private String[] decodeIpAddress(String addr){
        String[] res = new String[4];
        for(int i=0; i<4; i++){
            res[i] = addr.substring(2*i, 2*i+2);
        }
        return res;
    }
    // endregion

    private boolean verifyCheckSum(char[] codes){
        try{
            // IHL * 4 * 8 bit/Byte / (4 * 4 bin_bit/hex_bit)
            int len = Tools.hexChar2hex(this.IHL)*2;
            int sum = 0;
            for(int i=0; i<len; i++){
                sum += Tools.dec2hex(String.valueOf(codes, 4*i, 4));
            }
            int boundary = 65536; // 0xffff + 1
            sum = sum/boundary + sum%boundary;
            return sum == 0xffff;
        }catch (ConvertException e){
            return false;
        }
    }

    // region Display

    private String getValueToString(String title, String hexValue){
        StringBuilder res = new StringBuilder(title);
        res.append(" (0x").append(hexValue).append(") : ");
        try {
            res.append(Tools.dec2hex(hexValue)).append("\n");
        } catch (ConvertException e) {
            res.append("ERROR\n");
            this.log += (title + " - Error\n");
        }
        return res.toString();
    }

    private String getValueToString(String title, char hexValue){
        return getValueToString(title, String.valueOf(hexValue));
    }

    private String getVersionToString(){
        return getValueToString("IP Version", this.version);
    }

    private String getIHLToString(){
        StringBuilder res = new StringBuilder("Internet Header Length (0x");
        res.append(this.IHL).append(") : ");
        try {
            res.append(Tools.hexChar2hex(this.IHL)*4).append(" Byte\n");
        } catch (ConvertException e) {
            res.append("ERROR\n");
            this.log += "Header length - Error\n";
        }
        return res.toString();
    }

    private String getTosToString(){
        return getValueToString("Type of server", this.tos);
    }

    private String getTotalLengthToString(){
        return getValueToString("Total length", this.totLen);
    }

    private String getIdToString(){
        return getValueToString("Trusted Host ID", this.id);
    }

    private String getFlagsToString(){
        StringBuilder res = new StringBuilder("Flags : \n");
        res.append("\tReserved bit : ").append(this.flags[0]).append("\n");
        res.append("\tDo not fragment : ").append(this.flags[1]).append("\n");
        res.append("\tMore fragments : ").append(this.flags[2]).append("\n");
        return res.toString();
    }

    private String getOffsetToString(){
        return getValueToString("Fragment offset", this.offset);
    }

    private String getTTLToString(){
        return getValueToString("Time to live", this.TTL);
    }

    private String getProtocolToString(){
        StringBuilder res = new StringBuilder("Protocol");
        res.append(" (0x").append(this.protocol).append(") : ");
        res.append(this.str_protocol).append("\n");
        return res.toString();
    }

    private String getCheckSumToString(){
        StringBuilder res = new StringBuilder("Check Sum : ");
        res.append("0x").append(this.checkSum);
        if(isChecksumValid){
            res.append(" [Valid]\n");
        }else{
            res.append(" [non valid]\n");
        }
        return res.toString();
    }

    private String getSourceAddressToString(){
        StringBuilder res = new StringBuilder("Source IP address");
        res.append(" (").append(this.src[0]);
        for(int i=1; i<4; i++){
            res.append(".").append(this.src[i]);
        }
        res.append(") : ");
        try{
            res.append(Tools.dec2hex(this.src[0]));
            for(int i=1; i<4; i++){
                res.append(".").append(Tools.dec2hex(this.src[i]));
            }
            res.append("\n");
        }catch (ConvertException e){
            log += "Source IP address error.\n";
            res.append("ERROR\n");
        }
        return res.toString();
    }

    private String getDestinationAddressToString(){
        StringBuilder res = new StringBuilder("Destination IP address");
        res.append(" (").append(this.dest[0]);
        for(int i=1; i<4; i++){
            res.append(".").append(this.dest[i]);
        }
        res.append(") : ");
        try{
            res.append(Tools.dec2hex(this.dest[0]));
            for(int i=1; i<4; i++){
                res.append(".").append(Tools.dec2hex(this.dest[i]));
            }
            res.append("\n");
        }catch (ConvertException e){
            log += "Destination IP address error.\n";
            res.append("ERROR\n");
        }
        return res.toString();
    }

    private String getOptionToString(){
        if(this.option.equals("")){
            return "";
        }
        return "Option : "+this.option+"\n";
    }


    // endregion

    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append("Internet Protocol : \n");
        res.append(this.getVersionToString());
        res.append(this.getIHLToString());
        res.append(this.getTosToString());
        res.append(this.getTotalLengthToString());
        res.append(this.getIdToString());
        res.append(this.getFlagsToString());
        res.append(this.getOffsetToString());
        res.append(this.getTTLToString());
        res.append(this.getProtocolToString());
        res.append(this.getCheckSumToString());
        res.append(this.getSourceAddressToString());
        res.append(this.getDestinationAddressToString());
        res.append(this.getOptionToString());
        res.append("--------------------\n");
        res.append(data.toString());
        return res.toString();
    }


}
