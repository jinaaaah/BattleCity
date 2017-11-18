import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class RenderObject {

    protected int mode;
    protected HashMap<Integer, ArrayList<PImage>> imageMap;
    protected ResourceManger resourceManger;
    protected int posX, posY;
    protected PApplet pApplet;
    protected PImage pImage;
    protected ArrayList<PImage> pImages;
    protected int tick = 0;

    public RenderObject(PApplet pApplet, ResourceManger resourceManger){
        this.pApplet = pApplet;
        this.resourceManger = resourceManger;
        imageMap = new HashMap<>();
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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
    public void addMode(int mode, String resourceId, int[] indices){
        ArrayList<PImage> curImages = new ArrayList<>();

        for(int i = 0; i< indices.length; i++){
            //curImages[i] = resourceManager.getImage(resourceId,indices[i]);
        }
        imageMap.put(mode,curImages);
    }


}
