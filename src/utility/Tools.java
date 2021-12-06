package utility;

import exception.ConvertException;

public class Tools {
    public static int hexChar2hex(char c) throws ConvertException {
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

    public static int dec2hex(String str) throws ConvertException{
        int result = 0;
        for(int i=0; i<str.length(); i++){
            result = result * 16 + hexChar2hex(str.charAt(i));
        }
        return result;
    }
}
