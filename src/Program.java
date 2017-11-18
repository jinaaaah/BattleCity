import processing.core.PApplet;

import java.io.DataInputStream;
import java.io.IOException;

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

public class Program extends PApplet{
    private ResourceManger resourceManger;



    @Override
    public void settings() {
        size(500,500);
        resourceManger = new ResourceManger();
    }

    @Override
    public void draw() {
        background(0);
    }

    @Override
    public void setup() {
        background(0);
    }
    public static void main(String[] args) {
        PApplet.main("Program");
    }
}
