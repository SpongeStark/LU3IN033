package model;

import exception.ConvertException;
import utility.Tools;

import java.util.ArrayList;

public class DHCPOptions {
    ArrayList<String> ids;
    ArrayList<String> lengths;
    ArrayList<String> values;
    boolean isValid;


    public DHCPOptions(char[] codes){
        this.ids = new ArrayList<String>();
        this.lengths =  new ArrayList<String>();
        this.values = new ArrayList<String>();
        isValid = true;
        decode(codes);
    }

    public void decode(char[] codes){
        int offset = 0;
        String buffer = "00";
        while(!buffer.equals("ff") && offset < codes.length-2){
            // read option id
            ids.add(String.valueOf(codes, offset, 2));
            offset += 2;
            lengths.add(String.valueOf(codes, offset, 2));
            offset += 2;
            try{
                int len = Tools.hex2dec(lengths.get(lengths.size()-1));
                values.add(String.valueOf(codes, offset, 2*len));
                offset += 2*len;
            }catch (ConvertException e){
                isValid = false;
                return;
            }
            buffer = String.valueOf(codes, offset, 2);
        }
    }

    // region Options
    // There are more options, see http://www.networksorcery.com/enp/protocol/bootp/options.htm

    String getInfo(String id, String length, String value){
        StringBuilder res = new StringBuilder();
        switch (id){
            case "01": res.append(option_1(value)); break;
            case "32": res.append(option_50(value)); break;
            case "33": res.append(option_51(value)); break;
            case "35": res.append(option_53(value)); break;
            case "36": res.append(option_54(value)); break;
            case "37": res.append(option_55(length, value)); break;
            case "3a": res.append(option_58(value)); break;
            case "3b": res.append(option_59(value)); break;
            case "3d": res.append(option_61(length, value)); break;
            default:
                res.append("id : ").append(id).append("\n");
                res.append("\tLength : ").append(length).append("\n");
                res.append("\tValue : ").append(value).append("\n");
                break;
        }
        return res.toString();
    }

    String option_1(String value){
        StringBuilder res = new StringBuilder("Options(1) : Subnet Mask\n");
        try{
            res.append("\tLength : 4\n");
            res.append("\tMask : ").append(Tools.hex2dec(value.substring(0, 2)));
            for(int i=1; i<4; i++){
                res.append(".").append(Tools.hex2dec(value.substring(i, i+2)));
            }
            res.append("\n");
        }catch (Exception e){
            res.append("Error\n");
        }
        return res.toString();
    }

    String option_50(String value){
        StringBuilder res = new StringBuilder("Options(50) : Requested IP Address\n");
        try{
            res.append("\tLength : 4\n");
            res.append("\tAddress : ").append(Tools.hex2dec(value.substring(0, 2)));
            for(int i=1; i<4; i++){
                res.append(".").append(Tools.hex2dec(value.substring(2*i, 2*i+2)));
            }
            res.append("\n");
        }catch (Exception e){
            res.append("Error\n");
        }
        return res.toString();
    }

    String option_51(String value){
        StringBuilder res = new StringBuilder("Options(51) : IP Address Lease Time\n");
        try{
            res.append("\tLength : 4\n");
            res.append("\tTime : ").append(Tools.hex2dec(value)).append("s\n");
        }catch (Exception e){
            res.append("Error\n");
        }
        return res.toString();
    }

    String option_53(String value){
        return "Options(53) : DHCP Message Type\n" +
                "\tLength : 1\n" +
                "\tDHCP Type : " + getMsgType(value) + "\n";
    }

    String getMsgType(String id){
        switch (id){
            case "01": return "Discover";
            case "02": return "Offer";
            case "03": return "Request";
            case "04": return "Decline";
            case "05": return "ACK";
            case "06": return "NAK";
            case "07": return "Release";
            case "08": return "Inform";
            default: return "Error";
        }
    }

    String option_54(String value){
        StringBuilder res = new StringBuilder("Options(54) : DHCP Server Identifier\n");
        try{
            res.append("\tLength : 4\n");
            res.append("\tIdentifier : ").append(Tools.hex2dec(value.substring(0, 2)));
            for(int i=1; i<4; i++){
                res.append(".").append(Tools.hex2dec(value.substring(2*i, 2*i+2)));
            }
            res.append("\n");
        }catch (Exception e){
            res.append("Error\n");
        }
        return res.toString();
    }

    String option_55(String length, String value){
        StringBuilder res = new StringBuilder("Options(55) : Parameter request list\n");
        try{
            int len = Tools.hex2dec(length);
            res.append("\tLength : ").append(len).append("\n");
            for(int i=0; i<len; i++){
                int itemId = Tools.hex2dec(value.substring(2*i, 2*i+2));
                res.append("\tItem : (").append(itemId).append(") ")
                        .append(getParamRequestItem(itemId)).append("\n");
            }
        }catch (Exception e){
            res.append("Error\n");
        }
        return res.toString();
    }

    String getParamRequestItem(int id){
        //This function is not finished, more info : http://www.networksorcery.com/enp/rfc/rfc1533.txt
        switch (id){
            case 1: return "Subnet Mas";
            case 2: return "Time Offset";
            case 3: return "Router";
            case 4: return "Time Server";
            case 6: return "Domain Name Server";
            case 42: return "Network Time Protocol Server";
            default: return "";
        }
    }

    String option_58(String value){
        StringBuilder res = new StringBuilder("Options(58) : Renew time value\n");
        try{
            res.append("\tLength : 4\n");
            res.append("\tValue : ").append(Tools.hex2dec(value)).append("s\n");
        }catch (Exception e){
            res.append("Error\n");
        }
        return res.toString();
    }

    String option_59(String value){
        StringBuilder res = new StringBuilder("Options(59) : Rebinding time value\n");
        try{
            res.append("\tLength : 4\n");
            res.append("\tValue : ").append(Tools.hex2dec(value)).append("s\n");
        }catch (Exception e){
            res.append("Error\n");
        }
        return res.toString();
    }

    String option_61(String length, String value){
        StringBuilder res = new StringBuilder("Options(61) : Client Identifier\n");
        try{
            int len = Tools.hex2dec(length);
            res.append("\tLength : ").append(len).append("\n");
            res.append("\tHardware Type : ").append(value.substring(0, 2));
            if(value.substring(0, 2).equals("01")){
                res.append("(Ethernet)\n");
            }else{
                res.append("\n");
            }
            res.append("\tClient MAC Address : ");
            res.append(value.substring(2,4));
            for(int i=2; i<len; i++){
                res.append(":").append(value.substring(i*2, i*2+2));
            }
            res.append("\n");
        }catch (Exception e){
            res.append("Error\n");
        }
        return res.toString();
    }
    // endregion

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        if(isValid){
            for(int i=0; i<ids.size(); i++){
                res.append(getInfo(ids.get(i), lengths.get(i), values.get(i)));
            }
        }else{
            res.append("Options : Error\n");
        }
        return res.toString();
    }
}
