package view;

import javax.swing.*;

public class MainWindow extends JFrame {
    public JPanel container;
    public JButton btnFind;
    public JTextField txt_path;
    public JTextArea txt_output;
    public JButton btnCopy;
    public JButton btnSave;

    public MainWindow(){
        super();
        setWindow();
        setInputTextFiled();
        setBtnFind();
        setOutputArea();
    }

    private void setWindow(){
        setSize(600,400);
        this.setLocationRelativeTo(null); // 窗口居中显示
        this.setResizable(false); // 禁止调整窗口大小
        this.setTitle("Frame analyser - Youheng-Kai"); // 设置窗口标题
        container = new JPanel();
        this.setContentPane(container);
        container.setLayout(null);
    }

    private void setInputTextFiled(){
        txt_path = new JTextField();
        txt_path.setBounds(20, 20, 440, 30);
        container.add(txt_path);
    }

    private void setBtnFind(){
        btnFind = new JButton();
        btnFind.setBounds(480,17,100,35);
        btnFind.setText("Chose a file");
        container.add(btnFind);
    }

    private void setOutputArea(){
        txt_output = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txt_output);
//        scrollPane.setSize(90,30);
        scrollPane.setBounds(22, 60, 556, 200);
        container.add(scrollPane);
    }

    public void start(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
