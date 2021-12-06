package view;

import javax.swing.*;

public class MainWindow extends JFrame {
    public JButton btn;

    public MainWindow(){
        super();
        setWindow();
        setBtn();
    }

    private void setWindow(){
        setSize(400,300);
        setLayout(null);
    }

    private void setBtn(){
        btn = new JButton();
        btn.setSize(100,50);
        btn.setText("Chose a file");
        this.add(btn);
    }

    public void start(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
