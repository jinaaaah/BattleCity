import processing.core.PApplet;

public class Bullet extends RenderObject {

    public Bullet(PApplet p, int x, int y, int dir) {
        super(p);
        addMode(Constants.BULLET, Constants.OBJECT, 21, 21);
        addMode(Constants.EXPLOSION, Constants.OBJECT, 17, 19);
        setMode(Constants.BULLET);
        this.setSpeed(16/3);
        this.posX = x;
        this.posY = y;
        this.dir = dir;
    }

    @Override
    public void update() {
        if (dir == Constants.MOVE_LEFT) {
            posX -= speed;
        } else if (dir == Constants.MOVE_RIGHT) {
            posX += speed;
        } else if (dir == Constants.MOVE_UP) {
            posY -= speed;
        } else if (dir == Constants.MOVE_DOWN) {
            posY += speed;
        }
    }

    @Override
    public void render() {
        pImage = pImages.get(0);

        move();
    }

    public boolean checkPosition() {
        if(posX > 800 || posY > 800) {
            return true;
        }
        return false;
    }
}
