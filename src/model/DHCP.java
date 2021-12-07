package model;

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
    private String sname; // server host name
    private String file; // file name

    public DHCP(char[] codes){
        decode(codes);
    }


    @Override
    public void decode(char[] codes) {
        op = String.valueOf(codes, 0, 2);
        hType = String.valueOf(codes, 2, 2);
        hLen = String.valueOf(codes, 4, 2);
        hops = String.valueOf(codes, 6, 2);
        xid = String.valueOf(codes, 8, 8);
        secs = String.valueOf(codes, 16, 4);
        flags = String.valueOf(codes, 20, 4);
        ciaddr = decodeIpAddress(Arrays.copyOfRange(codes, 24, 24+16));
        yiaddr = decodeIpAddress(Arrays.copyOfRange(codes, 40, 40+16));
        siadde = decodeIpAddress(Arrays.copyOfRange(codes, 56, 56+16));
        giaddr = decodeIpAddress(Arrays.copyOfRange(codes, 72, 72+16));
        chaddr = decodeIpAddress(Arrays.copyOfRange(codes, 88, 88+16));
        sname = String.valueOf(codes, 104, 128);
        file = String.valueOf(codes, 232, 256);
    }

    String[] decodeIpAddress(char[] code){
        String[] res = new String[4];
        for(int i=0; i<4; i++){
            res[i] = String.valueOf(code, 4*i, 4);
        }
        return res;
    }

    // region Display
    String getOpToString(){
        String res = "";
        res += "OP : 0x"+op;
        if(op.equals("01")){
            res += " (Request)\n";
        }else{
            res += " (offer)\n";
        }
        return res;
    }

    // endregion

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Dynamic Host Configuration Protocol : \n");
        res.append(this.getOpToString());
        return res.toString();
    }
}
