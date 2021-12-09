package utility;

import exception.AsciizToStringException;
import exception.ConvertException;

public class Tools {
    public static int hex2dec(char c) throws ConvertException {
        if(c >= '0' && c <= '9'){
            return c - '0';
        }else if(c >= 'a' && c <= 'f'){
            return c - 'a' + 10;
        }else if(c >= 'A' && c <= 'F'){
            return c - 'A' + 10;
        }else{
            throw new ConvertException();
        }
    }

    public static int hex2dec(String str) throws ConvertException{
        return hex2dec(str.toCharArray());
    }

    public static int hex2dec(char[] charArray) throws ConvertException{
        int result = 0;
        for (char c : charArray) {
            result = result * 16 + hex2dec(c);
        }
        return result;
    }

    public static String asciiz2string(char[] asciiz) throws AsciizToStringException{
        StringBuilder res = new StringBuilder();
        int i = 0;
        String currentChar = String.valueOf(asciiz, i*2, 2);
        while(!currentChar.equals("00")){
            try{
                char c = (char)Tools.hex2dec(currentChar);
                res.append(c);
                i++;
                currentChar = String.valueOf(asciiz, i*2, 2);
            }catch (Exception e){
                throw new AsciizToStringException();
            }
        }
        return res.toString();
    }
}
