import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class RenderObject {

    protected int mode;
    protected HashMap<Integer, ArrayList<PImage>> imageMap;
    protected int posX, posY;
    protected int dir;
    protected boolean isMoved;
    protected PApplet pApplet;
    protected PImage pImage;
    protected int speed;
    protected ArrayList<PImage> pImages;
    protected int tick = 0;

    public RenderObject(PApplet pApplet){
        this.pApplet = pApplet;
        imageMap = new HashMap<>();
        isMoved = false;
    }

    public int getDir(){
        return dir;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setMoved(boolean moved) {
        isMoved = moved;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
    //그리기 업데이트
    public void update(){

    }

    //그리기
    public void render(){
        tick++;
        pImage = pImages.get(tick/ 8 % pImages.size());
        pApplet.image(pImage,posX,posY);
    }

    //mode 지정
    public void setMode(int mode){
        this.mode = mode;
        pImages = imageMap.get(mode);
    }

    //mode 추가
    public void addMode(int mode, int name, int start, int end) {
        ArrayList<PImage> img = new ArrayList<>();
        for(int i = start ; i <= end ; i++) {
            img.add(ResourceManager.getImage(name, i));
        }
        imageMap.put(mode, img);
    }

    public void drawImage() {
        pApplet.pushMatrix();
        if (this.dir == Constants.MOVE_LEFT) {
            pApplet.rotate(PApplet.radians(270.0f));
            pApplet.image(pImage, -posY, posX);
        } else if (this.dir == Constants.MOVE_RIGHT) {
            pApplet.rotate(PApplet.radians(90.0f));
            pApplet.image(pImage, posY, -posX);
        } else if (this.dir == Constants.MOVE_DOWN) {
            pApplet.rotate(PApplet.radians(180.0f));
            pApplet.image(pImage, -posX, -posY);
        } else {
            pApplet.image(pImage, posX, posY);
        }

        pApplet.popMatrix();
    }

    public boolean checkCollision(int pX, int pY){
        return false;
    }


}
