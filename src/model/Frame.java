package model;


public class Frame {
    String data_str;
    boolean isValid;
    String errorMessage;

    public Frame(){
        isValid = true;
        data_str="";
        errorMessage = "";
    }

    public void addCode(String code){
        if(isValid){
            data_str = data_str + code;
        }
    }

    /**
     * Test if a string is a Hex number of 1 Byte
     * @param codes
     * @return
     */
//    private boolean testCode(String code){
//        boolean result = code.length() == 2;
//        for(char c : code.toCharArray()){
//
//        }
//    }

    public void addCodes(String[] codes){
        for(String str : codes){
            this.addCode(str);
        }
    }

    public void setErrorMessage(String message){
        errorMessage = message;
        isValid = false;
    }

    public String toString(){
        StringBuilder buffer = new StringBuilder();
//        buffer.append("\nFrame\n");
        buffer.append("==================================================\n");
        if(isValid){
            buffer.append((new Ethernet(data_str.toLowerCase().toCharArray())).toString());
        }else{
            buffer.append("Frame not valid\n");
            buffer.append("Error message : ").append(errorMessage);
        }
        buffer.append("==================================================\n\n");
        return buffer.toString();
    }

}
