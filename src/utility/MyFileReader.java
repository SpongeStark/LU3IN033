package utility;

import exception.ConvertException;
import model.Frame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MyFileReader {
    private String path;
    private ArrayList<String> data;

    public MyFileReader(String path){
        this.path = path;
        data = new ArrayList<String>();
    }

    // 读取文件，并存储到data里
    public Boolean readFile() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;
            while ((str = in.readLine()) != null) {
                data.add(str.trim());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Frame[] getFrames(){
        ArrayList<Frame> result = new ArrayList<Frame>();
        Frame buffer = new Frame();
        String[] currentLine;
        String[] nextLine;
        // for the first line
        while(data.get(0).trim().equals("")){
            data.remove(0);
        }
        nextLine = data.get(0).trim().split(" ");

        for(int i=0; i<data.size()-1; i++){
            currentLine = nextLine;
            nextLine = data.get(i+1).trim().split(" ");
            while(data.get(i+1).trim().equals("")){//有空行就忽略往下走
                data.remove(i+1);
                nextLine = data.get(i+1).trim().split(" ");
            }
            if(i>=data.size()-1){
                break;
            }

            try{
                int currentOffset = Tools.hex2dec(currentLine[0]);
                int nextOffset = Tools.hex2dec(nextLine[0]);
                if(nextOffset == 0) { // 一个帧结束了
                    // 最后一行的数据推进缓存
                    buffer.addCodes(Arrays.copyOfRange(currentLine, 1, currentLine.length));
                    // 讲缓存添加到result里
                    result.add(buffer);
                    // 清空缓存
                    buffer = new Frame();
                }else{
                    int diffOffset = nextOffset - currentOffset;
                    if(diffOffset <= 0){ // offset不valid，忽略这一行
                        nextLine = currentLine;
                    }else{
                        currentLine = trimArray(currentLine);
                        if(currentLine.length >= diffOffset+1) {
                            buffer.addCodes(Arrays.copyOfRange(currentLine, 1, diffOffset + 1));
                        }else{
                            buffer.setErrorMessage("Error: in the line of offset = "+currentOffset);
                        }
                    }
                }
            }catch (ConvertException e){
                // offset无法识别，也直接忽略这一行
                nextLine = currentLine;
            } // END {try}
        } // END {for i from 0 to data.size()-2}
        // 读取最后一行：
        currentLine = data.get(data.size()-1).split(" ");
        buffer.addCodes(Arrays.copyOfRange(currentLine, 1, currentLine.length));
        result.add(buffer);
        return result.toArray(new Frame[0]);
    }

    private String[] trimArray(String[] array){
        ArrayList<String> res = new ArrayList<>();
        for(String s : array){
            if(!s.trim().isEmpty()){
                res.add(s.trim());
            }
        }
        return res.toArray(new String[0]);
    }

    public String toString(){
        StringBuilder strBuffer = new StringBuilder();
        for(String line : this.data){
            strBuffer.append(line).append("\n");
        }
        return strBuffer.toString();
    }


}
