import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;

public class ResourceManager {
    private static PApplet p;
    private static HashMap<Integer, ArrayList<PImage>> imageBox;

    public static void init(PApplet applet) {
        p = applet;
        imageBox = new HashMap<>();
    }

    public static void cropImage(int name, String path, int width, int height, int countX, int countY) {
        ArrayList<PImage> images = new ArrayList<>();
        for(int i = 0 ; i < countY ; i++) {
            for(int j = 0 ; j < countX ; j++) {
                PImage pImage = p.loadImage(path).get(j * width, i * height, width, height);
                pImage.resize(40,40);
//                p.pushMatrix();
//                p.rotate(PApplet.radians(90.0f));
//                p.popMatrix();
                images.add(pImage);
            }
        }
        imageBox.put(name, images);
    }

    public static PImage getImage(int name, int index) {
        return imageBox.get(name).get(index);
    }

    public static ArrayList<PImage> getImage(int name) {
        return imageBox.get(name);
    }
}
