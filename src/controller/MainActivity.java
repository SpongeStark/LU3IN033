package controller;

import view.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainActivity {
    MainWindow window;

    public MainActivity() {
        window = new MainWindow();
        window.btn.addActionListener(this::btn_onClick);
    }

    private void btn_onClick(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Ton message");
    }

    public void start() {
        window.start();
    }
}
