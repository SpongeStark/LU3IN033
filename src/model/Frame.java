package model;


public class Frame {
    String data_str;
    boolean isValid;
    String errorMessage;

    public Frame(){
        isValid = true;
        errorMessage = new String();
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
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.data_str).append("\n");
        buffer.append("is Valid = ").append(isValid).append("\n");
        buffer.append("Error message : ").append(errorMessage);
        return buffer.toString();
    }

}
