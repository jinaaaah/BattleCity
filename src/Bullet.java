import processing.core.PApplet;

public class Bullet extends RenderObject {
    public Bullet(PApplet pApplet) {
        super(pApplet);
        addMode(Constants.BULLET, Constants.OBJECT, 20, 20);
        addMode(Constants.EXPLOSION, Constants.OBJECT, 17, 19);
    }
}
