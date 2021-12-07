package model;

import exception.ConvertException;
import utility.Tools;

import java.security.PrivateKey;
import java.util.Arrays;
import java.util.PrimitiveIterator;

public class DNS implements Message {

    // DNS Header
    private String id;
    private boolean flagsStatus;
    private boolean qr;
    private int opCode; // operation code
    private boolean aa; // authoritative answer
    private boolean tc; // truncated
    private boolean rd; // recursion desired
    private boolean ra; // recursion available
    private int reserved;
    private int rCode; // response code
    private int questionCnt; // Question Count
    private String answerRRs; // Answer Count
    private String authRRs; // Authority record count
    private String addiRRs; // additional record count

    // Question
    DNSQuestions questions;

    private String log;
    private boolean isValid;


    public DNS(char[] codes){
        decode(codes);
        this.log = "";
    }

    // region Decode

    @Override
    public void decode(char[] codes) {
        this.id = String.valueOf(codes, 0, 4);
        this.flagsStatus = decodeFlags(Arrays.copyOfRange(codes, 4, 4+4));
        this.questionCnt = decodeQuestionCnt(Arrays.copyOfRange(codes, 8, 8+4));
        this.answerRRs = String.valueOf(codes, 12, 4);
        this.authRRs = String.valueOf(codes, 16, 4);
        this.addiRRs = String.valueOf(codes, 20, 4);
        int offset = this.decodeQuestions(Arrays.copyOfRange(codes, 24, codes.length));

    }

    private boolean decodeFlags(char[] codes){
        try{
            int temp = Tools.dec2hex(String.valueOf(codes));
            rCode = temp % 0b10000;
            temp /= 0b10000;
            reserved = temp % 0b1000;
            temp /= 0b1000;
            ra = (temp % 0b10 == 1);
            temp /= 0b10;
            rd = (temp % 0b10 == 1);
            temp /= 0b10;
            tc = (temp % 0b10 == 1);
            temp /= 0b10;
            aa = (temp % 0b10 == 1);
            temp /= 0b10;
            opCode = temp % 0b10000;
            temp /= 0b10000;
            qr = (temp % 0b10 == 1);
            return true;
        }catch (ConvertException e){
            this.log += "Flags - Error\n";
            return false;
        }
    }

    private int decodeQuestionCnt(char[] code){
        try{
            return Tools.dec2hex(String.valueOf(code));
        }catch (ConvertException e){
            return -1;
        }
    }

    // return the cursor of the answer
    private int decodeQuestions(char[] codes){
        questions = new DNSQuestions(questionCnt);
        return questions.decode(codes);
    }

    private int decodeAnswers(char[] codes){
        return 0;
    }
    // endregion

    // region Display
    private String getIdToString(){
        return "Identifier : 0x" + this.id + "\n";
    }

    private String getFlagsToString(){
        StringBuilder res = new StringBuilder("Flags : \n");
        if(!this.flagsStatus){
            res.append("\tError\n");
            return res.toString();
        }
        res.append("\tQR : ").append(qr?1:0).append(qr?" (Response)\n":" (Query)\n");
        res.append("\tOpcode : ").append(opCode).append("\n");
        res.append("\tAuthoritative answer : ").append(aa).append("\n");
        res.append("\tTruncated : ").append(tc).append("\n");
        res.append("\tRecursion desired : ").append(rd).append("\n");
        res.append("\tRecursion available : ").append(ra).append("\n");
        res.append("\tReserved : ").append(reserved).append("\n");
        res.append("\tResponse code : ").append(rCode).append("\n");
        return res.toString();
    }

    private String getQuestionCountToString(){
        if (questionCnt < 0) {
            return "Question count : Error";
        } else {
            return "Question count : "+questionCnt+"\n";
        }
    }

    private String getQuestionsToString(){
        return questions.toString();
    }

    // endregion

    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append("Domain Name System : \n");
        res.append(this.getIdToString());
        res.append(this.getFlagsToString());
        res.append(this.getQuestionCountToString());
        res.append(this.getQuestionsToString());
        return res.toString();
    }

}
