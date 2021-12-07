package model;

import exception.ConvertException;
import utility.Tools;

public class DNSQuestions {
    int n;
    String[] _names;
    String[] _types;
    String[] _classes;
    boolean isValid;

    public DNSQuestions(int questionCnt){
        this.n = questionCnt;
        _names = new String[this.n];
        _types = new String[this.n];
        _classes = new String[this.n];
        isValid = true;
    }

    public int decode(char[] codes){
        try {
            int cursor = 0;
            for (int i = 0; i < this.n; i++) {
                // decode name
                _names[i] = "";
                int length = Tools.dec2hex(String.valueOf(codes, cursor, 2));
                cursor += 2;
                do {
                    for (int j = 0; j < length; j++) {
                        _names[i] += (char) Tools.dec2hex(String.valueOf(codes, cursor, 2));
                        cursor += 2;
                    }
                    _names[i] += ".";
                    length = Tools.dec2hex(String.valueOf(codes, cursor, 2));
                    cursor += 2;
                } while (length != 0);
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
