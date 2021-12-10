package model;

import com.sun.xml.internal.xsom.impl.scd.Step;
import utility.Tools;

import javax.tools.Tool;

public class DNSSrcRecord {
    int n;
    int offset;
    String title;
    String[] _names;
    String[] _types;
    String[] _classes;
    int[] _ttl;
    int[] _srcDataLength;
    String[] _srcData;
    char[] data;
    boolean isValid;


    public DNSSrcRecord(int offset, int count, String title){
        this.n = count;
        this.title = title;
        _names = new String[n];
        _types = new String[n];
        _classes = new String[n];
        _ttl = new int[n];
        _srcDataLength = new int[n];
        _srcData = new String[n];
        this.offset = offset;
        isValid = count>=0;
    }

    public int decode(char[] codes){
        this.data = codes;
        try{
            int cursor = this.offset;
            for(int i=0; i<this.n; i++){
                // read name
                _names[i] = "";
                int length = Tools.hex2dec(String.valueOf(codes, cursor, 2));
                cursor += 2;
                do{
                    if(length>=0b11000000){ //DNS Message Compression
                        int denom = 0b100 * 0b10000 * 0b10000 * 0b10000;
                        int newOffset = Tools.hex2dec(String.valueOf(codes, cursor-2, 4)) % denom;
                        _names[i] += DNS.decodeDomainName(codes, newOffset*2);
                        cursor += 2;
                        break;
                    }else{
                        for (int j = 0; j < length; j++) {
                            _names[i] += (char) Tools.hex2dec(String.valueOf(codes, cursor, 2));
                            cursor += 2;
                        }
                        _names[i] += ".";
                        length = Tools.hex2dec(String.valueOf(codes, cursor, 2));
                        cursor += 2;
                    }
                } while (length != 0);
                // read type
                _types[i] = DNS.getType(String.valueOf(codes, cursor,  4));
                cursor += 4;
                // read class
                _classes[i] = DNS.getClass(String.valueOf(codes, cursor, 4));
                cursor += 4;
                // read ttl
                _ttl[i] = Tools.hex2dec(String.valueOf(codes, cursor, 8));
                cursor += 8;
                // read resource data length
//                _srcDataLength[i] = String.valueOf(codes, cursor, 4);
                int len = Tools.hex2dec(String.valueOf(codes, cursor, 4));
                cursor += 4;
                _srcDataLength[i] = len;
//                int len = Tools.hex2dec(_srcDataLength[i]);
                // read resource data
                String info = String.valueOf(codes, cursor, len*2);
                if(_types[i].equals("A")){_srcData[i] = getIpToString(info);}
                else if(_types[i].equals("CNAME")){_srcData[i] = DNS.decodeDomainName(codes, cursor);}
                else{_srcData[i] = info;}
                cursor += len*2;
            }
            return cursor;
        }catch (Exception e){
            isValid = false;
            return 0;
        }
    }


    // region Display
    String getIpToString(String ip){
        String[] ipAddr_str = new String[4];
        int[] res = new int[4];
        try{
            for(int i=0; i<4; i++){
                ipAddr_str[i] = ip.substring(2*i, 2*i+2);
                res[i] = Tools.hex2dec(ipAddr_str[i]);
            }
            return String.format("%d.%d.%d.%d",res[0],res[1],res[2],res[3]);
        }catch (Exception e){
            return "Error";
        }
    }



    // endregion
    public String toString(){
        StringBuilder res = new StringBuilder(this.title).append(" : ");
        if(isValid){
            res.append("\n");
            for(int i = 0; i< n; i++){
                res.append("--").append(i+1).append("----\n");
                res.append("    Name : ").append(_names[i]).append("\n");
                res.append("    Type : ").append(_types[i]).append("\n");
                res.append("    Class : ").append(_classes[i]).append("\n");
                res.append("    Time to live : ").append(_ttl[i]).append("\n");
                res.append("    Resource Data Length : ").append(_srcDataLength[i]).append("\n");
                res.append("    Resource Data : ").append(_srcData[i]).append("\n");
            }
        }else{
            res.append("Error\n");
        }
        return res.toString();
    }

}
