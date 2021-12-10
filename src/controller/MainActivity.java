package controller;

import model.Frame;
import utility.MyFileReader;
import view.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;

public class MainActivity {
    MainWindow window;

    public MainActivity() {
        window = new MainWindow();
        window.btnFind.addActionListener(this::btnFind_onClick);
        window.btnStart.addActionListener(this::btnStart_onCLick);
    }

    private void btnFind_onClick(ActionEvent e) {
//        JOptionPane.showMessageDialog(null, "Ton message");
        JFileChooser fileChooser = new JFileChooser();
        //file name filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file","txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(window);
        if(result == JFileChooser.APPROVE_OPTION){
            window.setPath(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void btnStart_onCLick(ActionEvent e){
        String path = window.getPath();
        if(!path.isEmpty()){
            window.clearOutput(); // 先清除输出
            MyFileReader file = new MyFileReader(path);
            if(file.readFile()){
                try{
                    Frame[] frames = file.getFrames();
                    for(int i=0; i<frames.length; i++){
                        window.appendOutput("Frame "+(i+1)+" :\n")
                                .appendOutput(frames[i].toString())
                                .appendOutput("\n");
                    }
                }catch (Exception ex){
                    window.appendOutput("decoding - Error");
                }
            }else{
                window.appendOutput("File reading - Error");
            }
        }else{
            window.appendOutput("File path - Error");
        }
    }

    public void start() {
        window.start();
    }
}
