package controller;

import model.Frame;
import utility.MyFileReader;
import view.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity {
    MainWindow window;

    public MainActivity() {
        window = new MainWindow();
        window.btnFind.addActionListener(this::btnFind_onClick);
        window.btnStart.addActionListener(this::btnStart_onCLick);
        window.btnCopy.addActionListener(this::btnCopy_onCLick);
        window.btnSave.addActionListener(this::btnSave_onCLick);
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
            btnStart_onCLick(e);
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

    private void btnCopy_onCLick(ActionEvent e){
        String result = window.getResult();
        // get system clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // encapsulate the result
        Transferable text = new StringSelection(result);
        clipboard.setContents(text, null);
        JOptionPane.showMessageDialog(window, "The result is in your clipboard");
    }

    private void btnSave_onCLick(ActionEvent e){
        JFileChooser fileChooser = new JFileChooser();
        //file name filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file","txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showSaveDialog(window);
        if(result == JFileChooser.APPROVE_OPTION){
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            path = getPathWithExtensionName(path, "txt");
            try{
                BufferedWriter out = new BufferedWriter(new FileWriter(path));
                out.write(window.getResult());
                out.close();
                JOptionPane.showMessageDialog(window, "Success");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(window, "Can't write in the file");
            }
        }
    }

    private String getPathWithExtensionName(String path, String extension){
        String[] pathSplit = path.split("\\.");
        if(pathSplit[pathSplit.length-1].trim().equals(extension)){
            return path;
        }
        return path+"."+extension;
    }


    public void start() {
        window.start();
    }
}
