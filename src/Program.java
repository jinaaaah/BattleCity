import processing.core.PApplet;
import processing.event.KeyEvent;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

interface OnRecieved {
    void onReceive(String packet);
}

class ReaderThread extends Thread{
    private InputStream is;
    private DataInputStream dis;
    private OnRecieved onRecieved;

    public ReaderThread(InputStream is){
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
        dis = new DataInputStream(is);
        byte[] data = new byte[1024];
        try{
            while (true) {
                int packetLen = dis.readInt();
                readn(dis, data,packetLen);

                String message = new String(data, 0, packetLen);
                if(onRecieved != null){
                    onRecieved.onReceive(message);
                }
            }
        }catch (EOFException ignored){
            System.out.println("연결 종료");
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                dis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Program extends PApplet {

    private static int[][] mapArray;
    private Socket socket;
    private ReaderThread readerThread;

    private List<Tank> tanks;
    private List<Block> blocks;
    private List<Bullet> bullets;

    private Tank user;

    private OnRecieved onRecieved = new OnRecieved() {
        @Override
        public void onReceive(String packet) {
            //System.out.println(packet);
            String[] messages = packet.split("#");

            if(messages[0].equals("MAP")){
                initMap(messages[1]);
            }
        }

    };

    @Override
    public void keyPressed(KeyEvent event) {
        //key에 따른 유저 업데이트, 가는 상태
        if(key == CODED){
            user.setMoved(true);
            if(keyCode == UP){
                user.setDir(Constants.MOVE_UP);
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
        user.setMoved(false);

    }

    @Override
    public void settings() {
        size(800, 800);
        ResourceManager.init(this);
        ResourceManager.cropImage(Constants.OBJECT, "./img/tanks_image.png", 85, 80, 8, 4);
        user = new Tank(this);

        mapArray = new int[20][20];
        tanks = new ArrayList<>();
        bullets = new ArrayList<>();
        blocks = new ArrayList<>();

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("192.168.11.3",5000));

            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            InputStream is = socket.getInputStream();

            readerThread = new ReaderThread(is);
            readerThread.setOnReceived(onRecieved);
            readerThread.start();


            String string2 = "MAP";
            dos.writeInt(string2.length());
            dos.write(string2.getBytes());

            String string = "SET#JiDah";
            dos.writeInt(string.length());
            dos.write(string.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw() {
        background(0);
        user.update();
        user.render();

        for(Bullet b : bullets){
            b.update();
        }
        for(Bullet b : bullets){
            b.render();
        }
    }

    @Override
    public void setup() {
        background(0);
    }

    //map 정보 받아와 blocks에 넣어준다
    private void initMap(String message){
        String[] row = message.split("/n");
        for(int i = 0; i < row.length; i++){
            System.out.println(row[i]);
            String[] column = row[i].split("");
            for(int j = 0; j < row[i].length(); j++){
                System.out.println(column[j]);
                //mapArray[i][j] = Integer.parseInt(column[j]);
            }
        }

    }

    public static void main(String[] args) {
        PApplet.main("Program");
    }
}