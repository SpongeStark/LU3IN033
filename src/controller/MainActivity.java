package controller;

import view.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;

public class MainActivity {
    MainWindow window;

    public MainActivity() {
        window = new MainWindow();
        window.btnFind.addActionListener(this::btnFind_onClick);
    }

    private void btnFind_onClick(ActionEvent e) {
//        JOptionPane.showMessageDialog(null, "Ton message");
        JFileChooser fileChooser = new JFileChooser();
        //file name filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file","txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(window);
        if(result == JFileChooser.APPROVE_OPTION){
            window.txt_path.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
//        FileDialog fileChooser = new FileDialog(window);
        //file name filter
//        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file","txt");
//        fileChooser.setFileFilter(filter);
//        fileChooser.show();
//        fileChooser.setMode(FileDialog.LOAD);
//        String filePath = fileChooser.getDirectory();
//        String fileName = fileChooser.getFile();
//        if(filePath!=null && fileName!=null){
//            window.txt_path.setText(filePath+fileName);
//        }



    }

    public void start() {
        window.start();
    }
}
