package model;

import exception.AsciizToStringException;
import exception.ConvertException;
import utility.Tools;

import java.util.Arrays;

public class DHCP implements Message{

    private String op; // opcode
    private String hType; // hardware type
    private String hLen; // hardware length
    private String hops;
    private String xid;
    private String secs; // seconds
    private String flags;
    private String[] ciaddr; // client ip address
    private String[] yiaddr; //your client ip address
    private String[] siadde; // server ip address
    private String[] giaddr; // gateway ip address
    private String[] chaddr; // client hardware address
    private String sName; // server host name
    private String fName; // file name
    private DHCPOptions options;

    public DHCP(char[] codes){
        decode(codes);
    }


    @Override
    public void decode(char[] codes) {
        int offset = 0;
        op = String.valueOf(codes, offset, 2);
        offset += 2;
        hType = String.valueOf(codes, offset, 2);
        offset += 2;
        hLen = String.valueOf(codes, offset, 2);
        offset += 2;
        hops = String.valueOf(codes, offset, 2);
        offset += 2;
        xid = String.valueOf(codes, offset, 8);
        offset += 8;
        secs = String.valueOf(codes, offset, 4);
        offset += 4;
        flags = String.valueOf(codes, offset, 4);
        offset += 4;
        ciaddr = decodeIpAddress(Arrays.copyOfRange(codes, offset, offset+8));
        offset += 8;
        yiaddr = decodeIpAddress(Arrays.copyOfRange(codes,  offset, offset+8));
        offset += 8;
        siadde = decodeIpAddress(Arrays.copyOfRange(codes,  offset, offset+8));
        offset += 8;
        giaddr = decodeIpAddress(Arrays.copyOfRange(codes,  offset, offset+8));
        offset += 8;
        chaddr = decodeChaddr(Arrays.copyOfRange(codes,  offset, offset+32));
        offset += 32;
        sName = String.valueOf(codes, offset, 128);
        offset += 128;
        fName = String.valueOf(codes, offset, 256);
        offset += 256;
        offset += 8; // Magic Cookie
        options = new DHCPOptions(Arrays.copyOfRange(codes, offset, codes.length));
    }

    // region Decode
    String[] decodeIpAddress(char[] code){
        String[] res = new String[4];
        for(int i=0; i<4; i++){
            res[i] = String.valueOf(code, 2*i, 2);
        }
        return res;
    }

    String[] decodeChaddr(char[] code){
        String[] res = new String[7];
        for(int i=0; i<6; i++){
            res[i] = String.valueOf(code, 2*i, 2);
        }
        res[6] = String.valueOf(code, 2*6, code.length-2*6);
        return res;
    }



    // endregion

    // region Display
    String getOpToString(){
        String res = "OP : 0x"+op;
        if(op.equals("01")){
            res += " (Request)\n";
        }else{
            res += " (offer)\n";
        }
        return res;
    }

    String getHTypeToString(){
        String res = "Hardware type : 0x"+hType;
        if(op.equals("01")){
            res += " (Ethernet)\n";
        }else{
            res += "\n";
        }
        return res;
    }

    String getHLenToString(){
        StringBuilder res = new StringBuilder("Hardware length (0x"+hLen+") : ");
        try {
            res.append(Tools.hex2dec(hLen)).append("\n");
        } catch (ConvertException e) {
            res.append("Error\n");
        }
        return res.toString();
    }

    String getHopsToString(){
        StringBuilder res = new StringBuilder("Hops (0x"+hops+") : ");
        try {
            res.append(Tools.hex2dec(hops)).append("\n");
        } catch (ConvertException e) {
            res.append("Error\n");
        }
        return res.toString();
    }

    String getXidToString(){
        return "Xid : 0x"+xid+"\n";
    }

    String getSecsToString(){
        StringBuilder res = new StringBuilder("Seconds (0x"+secs+") : ");
        try {
            res.append(Tools.hex2dec(secs)).append("\n");
        } catch (ConvertException e) {
            res.append("Error\n");
        }
        return res.toString();
    }

    String getFlagToString(){
        StringBuilder res = new StringBuilder("Flag : 0x").append(flags);
        try{
            if(Tools.hex2dec(flags.charAt(3))/0b100 == 1){
                res.append(" (Broadcast)\n");
            }else{
                res.append(" (Unicast)\n");
            }
        }catch (ConvertException e){
            res.append("Error\n");
        }
        return res.toString();
    }

    String getIpAddressToString(String name, String[] addr){
        StringBuilder res = new StringBuilder(name);
        res.append(" (").append(addr[0]);
        for(int i=1; i<4; i++){
            res.append(".").append(addr[i]);
        }
        res.append(") : ");
        try{
            res.append(Tools.hex2dec(addr[0]));
            for(int i=1; i<4; i++){
                res.append(".").append(Tools.hex2dec(addr[i]));
            }
            res.append("\n");
        }catch (ConvertException e){
            res.append("ERROR\n");
        }
        return res.toString();
    }

    String getChaddrToString(){
        StringBuilder res = new StringBuilder("Client Hardware address");
        res.append(" (").append(chaddr[0]);
        for(int i=1; i<6; i++){
            res.append(":").append(chaddr[i]);
        }
        res.append(") : ");
        try{
            res.append(Tools.hex2dec(chaddr[0]));
            for(int i=1; i<6; i++){
                res.append(":").append(Tools.hex2dec(chaddr[i]));
            }
            res.append("\n");
        }catch (ConvertException e){
            res.append("ERROR\n");
        }
        res.append("Client Hardware address padding : ").append(chaddr[6]).append("\n");
        return res.toString();
    }

    String getNameToString(String title, String asciizName){
        StringBuilder res = new StringBuilder(title).append(" : ");
        try {
            String _name = Tools.asciiz2string(asciizName.toCharArray());
            if(_name.isEmpty()){
                res.append("Not given\n");
            }else{
                res.append(_name).append("\n");
            }
        } catch (AsciizToStringException e) {
            res.append("invalid string\n");
        }
        return res.toString();
    }

    // endregion

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Dynamic Host Configuration Protocol : \n");
        res.append(this.getOpToString());
        res.append(this.getHTypeToString());
        res.append(this.getHLenToString());
        res.append(this.getHopsToString());
        res.append(this.getXidToString());
        res.append(this.getSecsToString());
        res.append(this.getFlagToString());
        res.append(this.getIpAddressToString("Client IP address",ciaddr));
        res.append(this.getIpAddressToString("Your (client) IP address",yiaddr));
        res.append(this.getIpAddressToString("Server IP address",siadde));
        res.append(this.getIpAddressToString("Gateway IP address",giaddr));
        res.append(this.getChaddrToString());
        res.append(this.getNameToString("Server host name", sName));
        res.append(this.getNameToString("Boot file name", fName));
        res.append(this.options.toString());
        return res.toString();
    }
}
