package model;

import exception.ConvertException;
import utility.Tools;

import java.util.Arrays;

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
    private int answerRRs; // Answer Count
    private int authRRs; // Authority record count
    private int addiRRs; // additional record count

    // Question
    DNSQuestions questions;
    DNSSrcRecord answers;
    DNSSrcRecord authorities;
    DNSSrcRecord additional;

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
        this.questionCnt = decodeCnt(Arrays.copyOfRange(codes, 8, 8+4));
        this.answerRRs = decodeCnt(Arrays.copyOfRange(codes, 12, 12+4));
        this.authRRs = decodeCnt(Arrays.copyOfRange(codes, 16, 16+4));
        this.addiRRs = decodeCnt(Arrays.copyOfRange(codes, 20, 20+4));
        int offset = this.decodeQuestions(codes, 24);
        offset = this.decodeAnswers(codes, offset);
        offset = this.decodeAuthority(codes, offset);
        this.decodeAdditional(codes,offset);

    }

    private boolean decodeFlags(char[] codes){
        try{
            int temp = Tools.hex2dec(String.valueOf(codes));
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

    private int decodeCnt(char[] code){
        try{
            return Tools.hex2dec(String.valueOf(code));
        }catch (ConvertException e){
            return -1;
        }
    }

    // return the cursor of the answer
    private int decodeQuestions(char[] codes, int offset){
        if(questionCnt>0){
            questions = new DNSQuestions(offset, questionCnt);
            return questions.decode(codes);
        }
        return 0;
    }

    private int decodeAnswers(char[] codes, int offset){
        if(answerRRs>0){
            answers = new DNSSrcRecord(offset, answerRRs, "Answer");
            return answers.decode(codes);
        }
        return 0;
    }

    private int decodeAuthority(char[] codes, int offset){
        if(authRRs>0){
            authorities = new DNSSrcRecord(offset, authRRs, "Authority");
            return authorities.decode(codes);
        }
        return 0;
    }

    private void decodeAdditional(char[] codes, int offset){
        if(addiRRs>0){
            additional = new DNSSrcRecord(offset, addiRRs, "additional");
            additional.decode(codes);
        }
    }
    // endregion

    public static String decodeDomainName(char[] codes, int offset) throws ConvertException{
        StringBuilder res = new StringBuilder();
        if(Tools.hex2dec(codes[offset])>=0b1100){
            //DNS Message Compression
            int denom = 0b100 * 0b10000 * 0b10000 * 0b10000;
            int newOffset = Tools.hex2dec(String.valueOf(codes, offset, 4)) % denom;
            res.append(decodeDomainName(codes, newOffset*2));
        }else{
            int cursor = offset;
            int length = Tools.hex2dec(String.valueOf(codes, cursor, 2));
            if(length != 0){
                cursor += 2;
                for (int j = 0; j < length; j++) {
                    res.append((char) Tools.hex2dec(String.valueOf(codes, cursor, 2)));
                    cursor += 2;
                }
                res.append(".");
                res.append(decodeDomainName(codes, cursor));
            }
        }
        return res.toString();
    }

    public static String getType(String typeId){
        switch (typeId){
            case "0001": return "A";
            case "0005": return "CNAME";
            default: return typeId;
        }
    }

    public static String getClass(String classId){
        switch (classId){
            case "0001": return "IN";
            default: return classId;
        }
    }

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
        res.append("    QR : ").append(qr?1:0).append(qr?" (Response)\n":" (Query)\n");
        res.append("    Opcode : ").append(opCode).append("\n");
        res.append("    Authoritative answer : ").append(aa).append("\n");
        res.append("    Truncated : ").append(tc).append("\n");
        res.append("    Recursion desired : ").append(rd).append("\n");
        res.append("    Recursion available : ").append(ra).append("\n");
        res.append("    Reserved : ").append(reserved).append("\n");
        res.append("    Response code : ").append(rCode).append("\n");
        return res.toString();
    }

    private String getQuestionCountToString(){
        if (questionCnt < 0) {
            return "Question count : Error";
        } else {
            return "Question count : "+questionCnt+"\n";
        }
    }

    private String getAnswerCountToString(){
        if (answerRRs < 0) {
            return "Answer count : Error";
        } else {
            return "Answer count : "+answerRRs+"\n";
        }
    }

    private String getAuthCountToString(){
        if (authRRs < 0) {
            return "Authority count : Error";
        } else {
            return "Authority count : "+authRRs+"\n";
        }
    }

    private String getAddiCountToString(){
        if (addiRRs < 0) {
            return "Additional count : Error";
        } else {
            return "Additional count : "+addiRRs+"\n";
        }
    }

    // endregion

    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append("Domain Name System : \n");
        res.append(this.getIdToString());
        res.append(this.getFlagsToString());
        res.append(this.getQuestionCountToString());
        res.append(this.getAnswerCountToString());
        res.append(this.getAuthCountToString());
        res.append(this.getAddiCountToString());
        res.append(questionCnt>0 ? questions.toString() : "");
        res.append(answerRRs>0 ? answers.toString() : "");
        res.append(authRRs>0 ? authorities.toString() : "");
        res.append(addiRRs>0 ? additional.toString() : "");
        return res.toString();
    }

}
