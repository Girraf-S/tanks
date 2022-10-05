package org.example;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow(){
        setTitle("Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//завершение програмы после нажатия красного крестика
        setSize(320*2, 345*2); //размер окна                                  в правом верхнем углу экрана
        setLocation(400, 400);// точка спауна
        add(new Gamefield());
        setVisible(true);
    }
    public static void main(String[] args) {
        MainWindow mainWindow=new MainWindow();
    }
}
