package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyFileDialog extends MouseAdapter {
    private JTextField filePathField;
    private JFrame frame;
    private FileDialog fileDialog;
    private String filePath;
    private String fileName;

    public MyFileDialog(JTextField filePathField, JFrame frame){
        this.filePathField = filePathField;
        this.frame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        fileDialog = new FileDialog(frame);
        fileDialog.show();
        filePath = fileDialog.getDirectory();
        fileName = fileDialog.getFile();
    }
}
