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
        byte[] data = new byte[8192];
        try{
            while (true) {
                try {
                    int packetLen = dis.readInt();
                    readn(dis, data, packetLen);

                    String message = new String(data, 0, packetLen);
                    if (onRecieved != null) {
                        onRecieved.onReceive(message);
                    }
                } catch(IOException nowatch){

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

    private static String myId = "JiDah";
    private static int[][] mapArray;
    private Socket socket;
    private ReaderThread readerThread;

    private List<Tank> tanks;
    private List<Block> blocks;
    private List<Bullet> bullets;
    private List<Explosion> explosions;
    private DataOutputStream dos;

    private Tank user;

    private OnRecieved onRecieved = new OnRecieved() {
        @Override
        public void onReceive(String packet) {
            String[] messages = packet.split("#");

            if(messages[0].equals("MAP")){
                initMap(messages[1]);
            }
            if(messages[0].equals("GEN")){
                if(messages[4].equals(myId)){
                    user.setName(messages[4]);
                    user.setTeam(Integer.parseInt(messages[1]));
                    user.setPosX((int)(Double.parseDouble(messages[2])));
                    user.setPosY((int)(Double.parseDouble(messages[3])));
                    tanks.add(user);
                }else{
                    Tank tank = createTank();
                    tank.setName(messages[4]);
                    tank.setTeam(Integer.parseInt(messages[1]));
                    tank.setPosX((int)(Double.parseDouble(messages[2])));
                    tank.setPosY((int)(Double.parseDouble(messages[3])));
                    tanks.add(tank);
                }

            }
            if(messages[0].equals("UPDATE")){
                if(detectTank(messages[1]) != null){
                    if(!messages[1].equals(myId)) {
                        Tank tank = detectTank(messages[1]);
                        tank.setPosX((int) (Double.parseDouble(messages[2])));
                        tank.setPosY((int) (Double.parseDouble(messages[3])));
                        tank.setDir(setDirection(messages[4]));
                        tank.setMoved(setState(messages[5]));
                    }
                }else{
                    Tank tank = createTank();
                    tank.setName(messages[1]);
                    tank.setTeam(Integer.parseInt(messages[6]));
                    tank.setPosX((int)(Double.parseDouble(messages[2])));
                    tank.setPosY((int)(Double.parseDouble(   messages[3])));
                    tank.setDir(setDirection(messages[4]));
                    tank.setMoved(setState(messages[5]));
                    tanks.add(tank);
                }
                System.out.println(messages[1]+":"+messages[4]);
            }
            if(messages[0].equals("BULLET")){
                createBullet(messages[1],messages[2],messages[3],messages[4]);
            }
            if(messages[0].equals("DESTROY")){
                if(messages.length == 2){
                    deleteBullet(messages[1]);
                }
                else if(messages.length == 3){
                    if(messages[2].equals(myId)){
                        reset();
                    }
                    deleteBullet(messages[1]);
                    deleteTank(messages[2]);
                }

            }
            if(messages[0].equals("OUT")){
                deleteTank(messages[1]);
            }
        }

    };

    private void deleteBullet(String name){
        for(Bullet b: bullets){
            if(b.getName().equals(name)){
                bullets.remove(b);
                break;
            }
        }
    }

    private void deleteTank(String name){
        for(Tank tank: tanks){
            if(tank.getName().equals(name)){
                tanks.remove(tank);
                break;
            }
        }
    }
    private boolean setState(String message){
        if(message.equals("0")){
            return false;
        }else{
            return true;
        }
    }
    private int setDirection(String message){
        int i = 0;
        if(message.equals("LEFT")){
            i = Constants.MOVE_LEFT;
        }
        if(message.equals("RIGHT")){
            i = Constants.MOVE_RIGHT;
        }
        if(message.equals("UP")){
            i = Constants.MOVE_UP;
        }
        if(message.equals("DOWN")){
            i = Constants.MOVE_DOWN;
        }
        return i;
    }

    private Tank createTank(){
        Tank tank = new Tank(this);
        return tank;
    }

    private void createBullet(String name,String x, String y, String dir){
        int direction = setDirection(dir);

        boolean bulletExist = false;

        for(Bullet b: bullets){
            if(b.getName().equals(name)){
                b.setDir(direction);
                b.posX = (int)(Double.parseDouble(x));
                b.posY = (int)(Double.parseDouble(y));

                bulletExist = true;
            }
        }

        if(!bulletExist) {
            bullets.add(new Bullet(this, name, (int) (Double.parseDouble(x)),
                    (int) (Double.parseDouble(y)), direction));
        }

    }

    private Tank detectTank(String name){
        Tank tank = null;
        for(Tank t : tanks){
            if(t.getName().equals(name)){
                tank = t;
            }
        }
        return tank;
    }

    @Override
    public void keyPressed(KeyEvent event) {

        String string = null;
        try {

            String direction= null;
            //key에 따른 유저 업데이트, 가는 상태
            if(key == CODED){
                user.setMoved(true);
                if(keyCode == UP){
                    user.setDir(Constants.MOVE_UP);
                    direction = "UP";
                    checkCollision();
                }else if(keyCode == DOWN){
                    user.setDir(Constants.MOVE_DOWN);
                    direction = "DOWN";
                    checkCollision();
                }else if(keyCode == LEFT){
                    user.setDir(Constants.MOVE_LEFT);
                    direction = "LEFT";
                    checkCollision();
                }else if(keyCode == RIGHT){
                    user.setDir(Constants.MOVE_RIGHT);
                    direction = "RIGHT";
                    checkCollision();
                }
                string = "MOVE#" + direction + "#" + user.getPosX() + "#" + user.getPosY();
            }
            if(key == ' '){
                string = "BULLET";
            }

            dos.writeInt(string.length());
            dos.write(string.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void keyReleased() {
        try {
            String string = "STOP";
            dos.writeInt(string.length());
            dos.write(string.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
        }

        //멈춤상태
        user.setMoved(false);

    }

    private void checkCollision(){
        for(Block b: blocks){
            if(user.checkCollision(b.getPosX(), b.getPosY())){
                user.setMoved(false);
                return;
            }
        }

        user.setMoved(true);
    }

    private void reset(){
        settings();
    }

    @Override
    public void settings() {
        size(800, 800);
        ResourceManager.init(this);
        ResourceManager.cropImage(Constants.OBJECT, "./img/tanks_image.png", 84, 84, 8, 4);
        user = new Tank(this);

        mapArray = new int[20][20];
        tanks = new ArrayList<>();
        bullets = new ArrayList<>();
        blocks = new ArrayList<>();
        explosions = new ArrayList<>();

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("192.168.11.3",5000));

            OutputStream os = socket.getOutputStream();
            dos = new DataOutputStream(os);
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

        List<Tank> drawTanks = new ArrayList<>();
        drawTanks.addAll(tanks);

        List<Bullet> drawBullets = new ArrayList<>();
        drawBullets.addAll(bullets);

        for(Tank tank : drawTanks){
            tank.update();
        }
        for(Tank tank: drawTanks){
            tank.render();
        }

        for(Bullet bullet : drawBullets){
            bullet.update();
        }
        for(Bullet bullet: drawBullets){
            bullet.render();
        }

        for(Block b : blocks){
            b.render();
        }

        for(int i = 0 ; i < explosions.size() ; i++) {
            Explosion e = explosions.get(i);
            e.update();
            e.render();

            if(e.isRemoveOk()) {
                explosions.remove(e);
            }
        }
    }

    @Override
    public void setup() {
        background(0);
    }

    //map 정보 받아와 blocks에 넣어준다
    private void initMap(String message){
        String row = message;
        Block block;
        for(int i = 0 ; i < 20 ; i ++){
            for( int j = 0 ; j < 20 ; j ++){
                mapArray[i][j] = Integer.parseInt(String.valueOf(row.charAt(j * 21 + i)));
                if(mapArray[i][j] == 1){
                    block = new Block(this);
                    block.setPosX(i*40 +20);
                    block.setPosY(j*40 +20);
                    blocks.add(block);
                }
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("Program");
    }
}