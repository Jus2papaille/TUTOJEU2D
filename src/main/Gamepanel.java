package main;

import entity.Player;
import tile.TileManager;
import Object.SuperObject;

import java.awt.*;

import javax.swing.JPanel;
public class Gamepanel extends JPanel implements Runnable{

    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tiles
    final int scale = 3; // 3x scale

    public final int tileSize = originalTileSize * scale; // 48x48 pixels
    public final int maxScreenCol = 16; // 16 tiles wide
    public final int maxScreenRow = 12; // 12 tiles tall
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels tall

    //WORLD SETTINGS
    public final int maxWorldCol = 50; // 50 tiles wide
    public final int maxWorldRow = 50; // 50 tiles tall
    public final int worldWidth = tileSize * maxWorldCol; // 800 pixels wide
    public final int worldHeight = tileSize * maxWorldRow; // 800 pixels tall

    //FPS
    int fps = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];

    public Gamepanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){

        aSetter.setObject();
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
// PREMIERE METHOD SLEEP
//    @Override
//    public void run() {
//
//        double drawInterval = 1000000000/fps; // 0.0166666666666667 seconds
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//
//        while(gameThread != null){
//
//
//            update(); // UPDATE : update information such as character positions
//
//            repaint();// DRAW : draw the updated information to the screen
//
//
//             try {
//                 double remainingTime = nextDrawTime - System.nanoTime();
//                 remainingTime = remainingTime/1000000;
//
//                 if(remainingTime < 0){
//                     remainingTime = 0;
//                 }
//
//                 Thread.sleep((long)remainingTime);
//
//                 nextDrawTime += drawInterval;
//
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//        }
//    }

// DEUXIEME METHOD DELTA
    @Override
    public void run(){
        double DrawInterval = 1000000000/fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawcount = 0;

        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / DrawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;
                drawcount++;
            }

            if(timer >= 1000000000){
                System.out.println("FPS: " + drawcount);
                drawcount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        player.update();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        //tile
        tileM.draw(g2);

        //object
        for (int i = 0; i < obj.length ; i++) {
            if(obj[i] != null){
                obj[i].draw(g2, this);
            }
        }

        //player
        player.draw(g2);

        g2.dispose();
    }
}
