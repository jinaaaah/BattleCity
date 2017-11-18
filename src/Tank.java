import processing.core.PApplet;

public class Tank extends RenderObject {
    protected int team;
    public Tank(PApplet pApplet) {
        super(pApplet);
        this.setSpeed(1);
        addMode(Constants.MOVE_LEFT, Constants.OBJECT, new int[]{1,2,3,4,5,6,7,8});
    }

    @Override
    public void update() {
        if (this.dir == Constants.MOVE_LEFT) {
            posX -= speed;
        } else if (this.dir == Constants.MOVE_RIGHT) {
            posX += speed;
        } else if (this.dir == Constants.MOVE_UP) {
            posY -= speed;
        } else if (this.dir == Constants.MOVE_DOWN) {
            posY += speed;
        }
    }

    @Override
    public void render() {
        tick++;
        pImage = pImages.get((tick/8) % pImages.size());
        pApplet.pushMatrix();
        if (this.dir == Constants.MOVE_LEFT) {
            pApplet.rotate(270);
        } else if (this.dir == Constants.MOVE_RIGHT) {
            pApplet.rotate(90);
        } else if (this.dir == Constants.MOVE_DOWN) {
            pApplet.rotate(180);
        }
        pApplet.image(pImage, posX, posY);
        pApplet.popMatrix();
    }
}