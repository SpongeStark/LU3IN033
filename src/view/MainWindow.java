package view;

import javax.swing.*;

public class MainWindow extends JFrame {
    public JPanel container;
    public JButton btn;
    public JTextField txt_path;
    public JTextArea txt_output;

    public MainWindow(){
        super();
        setWindow();
        setInputTextFiled();
        setBtn();
        setOutputArea();
    }

    private void setWindow(){
        setSize(400,300);
        container = new JPanel();
        this.setContentPane(container);
        container.setLayout(null);
    }

    private void setInputTextFiled(){
        txt_path = new JTextField();
        txt_path.setBounds(20, 20, 250, 30);
        container.add(txt_path);
    }

    private void setBtn(){
        btn = new JButton();
//        btn.setSize(100,35);
        btn.setBounds(280,20,100,30);
        btn.setText("Chose a file");
        container.add(btn);
    }

    private void setOutputArea(){
        txt_output = new JTextArea("Ethernet");
        JScrollPane scrollPane = new JScrollPane(txt_output);
//        scrollPane.setSize(90,30);
        scrollPane.setBounds(20, 60, 360, 200);
        container.add(scrollPane);
    }

    public void start(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
