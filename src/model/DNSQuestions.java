package model;

import exception.ConvertException;
import utility.Tools;

import java.util.Arrays;

public class DNSQuestions {
    int n;
    int offset;
    String[] _names;
    String[] _types;
    String[] _classes;
    boolean isValid;

    public DNSQuestions(int offset, int questionCnt){
        this.offset = offset;
        this.n = questionCnt;
        _names = new String[this.n];
        _types = new String[this.n];
        _classes = new String[this.n];
        isValid = questionCnt >= 0;
    }

    public int decode(char[] codes){
        try {
            int cursor = this.offset;
            for (int i = 0; i < this.n; i++) {
                if(Tools.hex2dec(codes[cursor])>=12){
                    //DNS Message Compression
                    int denom = 0b100 * 0b10000 * 0b10000 * 0b10000;
                    int newOffset = Tools.hex2dec(String.valueOf(codes, cursor, 4)) % denom;
                    _names[i] = DNS.decodeDomainName(codes, newOffset*2);
                    cursor += 4;
                }else{
                    _names[i] = DNS.decodeDomainName(codes, cursor);
                    cursor += (_names[i].length()+1)*2;
                }
                // decode name
//                _names[i] = "";
//                int length = Tools.hex2dec(String.valueOf(codes, cursor, 2));
//                cursor += 2;
//                do {
//                    for (int j = 0; j < length; j++) {
//                        _names[i] += (char) Tools.hex2dec(String.valueOf(codes, cursor, 2));
//                        cursor += 2;
//                    }
//                    _names[i] += ".";
//                    length = Tools.hex2dec(String.valueOf(codes, cursor, 2));
//                    cursor += 2;
//                } while (length != 0);

                // decode type
                _types[i] = String.valueOf(codes, cursor,  4);
                cursor += 4;
                _classes[i] = String.valueOf(codes, cursor, 4);
                cursor += 4;
            }
            return cursor;
        }catch (ConvertException e){
            isValid = false;
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Questions : ");
        if(isValid){
            for(int i = 0; i< n; i++){
                res.append("\n--").append(i+1).append("----\n");
                res.append("\tName : ").append(_names[i]).append("\n");
                res.append("\tType : ").append(_types[i]).append("\n");
                res.append("\tClass : ").append(_classes[i]).append("\n");
            }
        }else{
            res.append("Error\n");
        }
        return res.toString();
    }
}
