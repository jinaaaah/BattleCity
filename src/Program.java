import processing.core.PApplet;
import processing.event.KeyEvent;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

interface OnRecieved {
    void onReceive(String packet);
}

class ReaderThread extends Thread{
    private DataInputStream is;
    private OnRecieved onRecieved;

    public ReaderThread(DataInputStream is){
        this.is = is;
    }

    public static void readn(DataInputStream is, byte[] data, int size) throws Exception{
        int left = size;
        int offset = 0;

        while (left > 0){
            int len = is.read(data,offset,left);
            left -= len;
            offset += len;
        }
    }

    void setOnReceived(OnRecieved onReceived){
        this.onRecieved = onReceived;
    }

    @Override
    public void run() {
        byte[] data = new byte[1024];
        try{
            while (true) {
                int packetLen = is.readInt();
                readn(is, data,packetLen);

                String message = new String(data, 0, packetLen);
                if(onRecieved != null){
                    onRecieved.onReceive(message);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Program extends PApplet {

    private Socket socket;
    private ReaderThread readerThread;
    private OutputStream os;

    private List<Tank> tanks;
    private List<Block> blocks;
    private List<Bullet> bullets;

    private Tank user;

    @Override
    public void keyPressed(KeyEvent event) {
        //key에 따른 유저 업데이트, 가는 상태
        if(key == CODED){
            if(keyCode == UP){
                user.setDir(Constants.MOVE_UP);
                System.out.println("test");
            }else if(keyCode == DOWN){
                user.setDir(Constants.MOVE_DOWN);
            }else if(keyCode == LEFT){
                user.setDir(Constants.MOVE_LEFT);
            }else if(keyCode == RIGHT){
                user.setDir(Constants.MOVE_RIGHT);
            }else if(key == 'd'){

            }
        }

    }

    @Override
    public void keyReleased() {
        //멈춤상태
//        int posX = user.getPosX();
//        int posY = user.getPosY();
//        if(keyCode == UP || keyCode == DOWN) {
//            user.setPosX(posY);
//            user.setPosY(posX);
//        }

        user.setDir(Constants.STOP);

    }

    @Override
    public void settings() {
        size(800, 800);
        ResourceManager.init(this);
        ResourceManager.cropImage(Constants.OBJECT, "./img/tanks_image.png", 85, 80, 8, 4);
        user = new Tank(this);

        tanks = new ArrayList<>();
        bullets = new ArrayList<>();
        blocks = new ArrayList<>();
    }

    @Override
    public void draw() {
        background(0);
        user.update();
        user.render();
    }

    @Override
    public void setup() {
        background(0);
    }

    //map 정보 받아와 blocks에 넣어준다
    private void initMap(){

    }

    public static void main(String[] args) {
        PApplet.main("Program");
    }
}