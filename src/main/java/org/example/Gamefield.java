package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Gamefield extends JPanel implements ActionListener{
    private final int SIZE = 640;
    private final int DOT_SIZE = 32;
    private final int ALL_DOTS=1600;

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private int t1=3;
    private int t2=3;

    private int n=5;

    private Timer timer=new Timer(150, this);

    private boolean inGame=true;

    private boolean shot1=false;
    private boolean shot2=false;
    private class Patron{
        public byte napr;
        public int px=2*SIZE;
        public int py=2*SIZE;
        private static Image image;
    }
    private class Tank{
        private boolean left;
        private boolean right;
        private boolean down;
        private boolean up;
        private byte shotCount;
        private int x;
        private int y;
        private Image image;
        public Patron[] patron;
        public Tank(){
            patron=new Patron[n];//={new Patron(), new Patron(),new Patron(),new Patron(),new Patron() };
            this.shotCount=0;
        }

    }
    Patron[] patron2=new Patron[n];

    Patron patrons[]=new Patron[n];
    Tank tank1=new Tank();
    public void loadImage(){
        ImageIcon iit1=new ImageIcon("tankR.png");
        tank1.image= iit1.getImage();
        ImageIcon iip1=new ImageIcon("tankG.png");
        Patron.image = iip1.getImage();
    }

    public void initGame(Timer timer){
        tank1.x=64;
        tank1.y=64;
        timer.start();
    }

    public void checkShot(){
        for (int i = 0; i < n; i++) {
            if(x1==patron2[i].px&&y1==patron2[i].py) t1--;
         //   if(x2==patron1[i].px&&y2==patron1[i].py) t2--;
            if(t1==0||t2==0){
                inGame=false;
                break;
            }
        }
    }
    public void checkBorder(){
        if(tank1.x<=0){
            tank1.x=0;
            tank1.left=false;
        }
        if(tank1.y<=0) {
            tank1.y = DOT_SIZE;
            tank1.up = false;
        }
        if(tank1.x>=SIZE) {
            tank1.x = SIZE - DOT_SIZE;
            tank1.right = false;
        }
        if(tank1.y>=SIZE) {
            tank1.y = SIZE - DOT_SIZE;
            tank1.down = false;
        }
    }

    public void checkShot(Tank tank){
        if(tank.shotCount!=0){
            for (int i = 0; i < tank.shotCount; i++) {
                if (tank.patron[i].px <= 0||tank.patron[i].py <= 0
                        ||tank.patron[i].px >= SIZE||tank.patron[i].py >= SIZE) {
                    for (int j = i; j < tank.shotCount-1; j++) {
                        tank.patron[j]=tank.patron[j+1];
                    }
                    tank.shotCount--;
                }

            }
        }
    }
    public void move(){

        if(tank1.left) tank1.x-=DOT_SIZE/2;
        if(tank1.right)tank1.x+=DOT_SIZE/2;
        if(tank1.up)tank1.y-=DOT_SIZE/2;
        if(tank1.down)tank1.y+=DOT_SIZE/2;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(inGame){
            g.drawImage(tank1.image, tank1.x, tank1.y, this);
            if(tank1.shotCount!=0)
                for (int i = 0; i < tank1.shotCount; i++)
                    g.drawImage(Patron.image, tank1.patron[i].px, tank1.patron[i].py, this);
        }else{
            String str="Game over";
            g.setColor(Color.red);
            g.drawString(str, SIZE/3, SIZE/2) ;
        }
    }
    public void patrNapr(Tank tank){
        int i=tank.shotCount;
        if(tank.left)tank.patron[i].napr=1;
        if(tank.up)tank.patron[i].napr=2;
        if(tank.right)tank.patron[i].napr=3;
        if(tank.down)tank.patron[i].napr=4;
        tank.patron[i].px=tank.x;
        tank.patron[i].py=tank.y;
    }
    public void takeShot(Tank tank){
    if(tank.shotCount!=0){
        for (int i = 0; i < tank.shotCount; i++) {

        switch (tank.patron[i].napr) {
            case 1:
                tank.patron[i].px -= DOT_SIZE;
                break;
            case 2:
                tank.patron[i].py -= DOT_SIZE;
                break;
            case 3:
                tank.patron[i].px += DOT_SIZE;
                break;
            case 4:
                tank.patron[i].py += DOT_SIZE;
                break;
        }
        }
    }
    }
    @Override
    public void actionPerformed(ActionEvent a){
        if(inGame){
            checkBorder();
            checkShot(tank1);
            move();
            takeShot(tank1);
        }
        repaint();
    }
    public Gamefield(){
        setBackground(Color.yellow);
        loadImage();
        initGame(timer);
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent k) {
            super.keyPressed(k);
            int key = k.getKeyCode();

            if(key==KeyEvent.VK_SHIFT){
                patrNapr(tank1);
                tank1.shotCount++;
            }
            if (key == KeyEvent.VK_LEFT && !tank1.right) {
                tank1.left = true;
                tank1.up = tank1.down = false;
            } else if (key == KeyEvent.VK_LEFT && tank1.right) {
                tank1.right = tank1.up = tank1.down = false;
            }

            if (key == KeyEvent.VK_RIGHT && !tank1.left) {
                tank1.right = true;
                tank1.up = tank1.down = false;
            } else if (key == KeyEvent.VK_RIGHT && tank1.left) {
                tank1.left = tank1.up = tank1.down = false;
            }

            if (key == KeyEvent.VK_UP && !tank1.down) {
                tank1.up = true;
                tank1.right = tank1.left = false;
            } else if (key == KeyEvent.VK_UP && tank1.down) {
                tank1.right = tank1.left = tank1.down = false;
            }

            if (key == KeyEvent.VK_DOWN && !tank1.up) {
                tank1.down = true;
                tank1.right = tank1.left = false;
            } else if (key == KeyEvent.VK_DOWN && tank1.up) {
                tank1.right = tank1.left = tank1.up = false;
            }

        }
    }

}
