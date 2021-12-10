package view;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public JPanel container;
    public JButton btnFind;
    private JTextField txt_path;
    private JTextArea txt_output;
    public JButton btnCopy;
    public JButton btnStart;
    public JButton btnSave;

    public MainWindow(){
        super();
        initWindow();
        initInputTextFiled();
        initBtnFind();
        initOutputArea();
        initBtnCopy();
        initBtnStart();
        initBtnSave();
    }

    private void initWindow(){
        setSize(600,400);
        this.setLocationRelativeTo(null); // 窗口居中显示
        this.setResizable(false); // 禁止调整窗口大小
        this.setTitle("Frame analyser - Youheng-Kai"); // 设置窗口标题
        container = new JPanel();
        this.setContentPane(container);
        container.setLayout(null);
    }

    private void initInputTextFiled(){
        txt_path = new JTextField();
        txt_path.setBounds(20, 20, 440, 30);
        container.add(txt_path);
    }

    public void setPath(String path){
        this.txt_path.setText(path);
    }

    public String getPath(){
        return this.txt_path.getText();
    }

    private void initBtnFind(){
        btnFind = new JButton();
        btnFind.setBounds(480,17,100,35);
        btnFind.setText("Chose a file");
        container.add(btnFind);
    }

    private void initOutputArea(){
        txt_output = new JTextArea();
        txt_output.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txt_output);
        scrollPane.setBounds(22, 60, 556, 240);
        container.add(scrollPane);
    }

    public void clearOutput(){
        this.txt_output.setText("");
    }

    public MainWindow appendOutput(String text){
        this.txt_output.append(text);
        return this;
    }

    public String getResult(){
        return this.txt_output.getText();
    }

    private void initBtnCopy(){
        btnCopy = new JButton();
        btnCopy.setBounds(100,310,100,35);
        btnCopy.setText("Copy");
        container.add(btnCopy);
    }

    private void initBtnStart(){
        btnStart = new JButton();
        btnStart.setBounds(250,310,100,35);
        btnStart.setText("Decode");
        container.add(btnStart);
    }

    private void initBtnSave(){
        btnSave = new JButton();
        btnSave.setBounds(400,310,100,35);
        btnSave.setText("Save");
        container.add(btnSave);
    }


    public void start(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
