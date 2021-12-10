package model;

import utility.Tools;

public class DNSAnswers {
    int n;
    int offset;
    String[] _names;
    String[] _types;
    String[] _classes;
    String[] _ttl;
    int[] _srcDataLength;
    String[] _srcData;
    char[] data;
    boolean isValid;


    public DNSAnswers(int offset, int answerCnt){
        this.n = answerCnt;
        _names = new String[n];
        _types = new String[n];
        _classes = new String[n];
        _ttl = new String[n];
        _srcDataLength = new int[n];
        _srcData = new String[n];
        this.offset = offset;
        isValid = answerCnt>=0;
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
                _types[i] = String.valueOf(codes, cursor,  4);
                cursor += 4;
                // read class
                _classes[i] = String.valueOf(codes, cursor, 4);
                cursor += 4;
                // read ttl
                _ttl[i] = String.valueOf(codes, cursor, 8);
                cursor += 8;
                // read resource data length
//                _srcDataLength[i] = String.valueOf(codes, cursor, 4);
                int len = Tools.hex2dec(String.valueOf(codes, cursor, 4));
                cursor += 4;
                _srcDataLength[i] = len;
//                int len = Tools.hex2dec(_srcDataLength[i]);
                // read resource data
                _srcData[i] = String.valueOf(codes, cursor, len*2);
                cursor += len*2;
            }
            return cursor;
        }catch (Exception e){
            isValid = false;
            return 0;
        }
    }


    // region Display

    // endregion
    public String toString(){
        StringBuilder res = new StringBuilder("Answers : ");
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
//        res.append(String.valueOf(this.data, offset, data.length-offset));
        return res.toString();
    }

}
