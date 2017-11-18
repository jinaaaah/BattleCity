import processing.core.PApplet;

public class Bullet extends RenderObject {

    private String name;
    public Bullet(PApplet p,String name, int x, int y, int dir) {
        super(p);
        addMode(Constants.BULLET, Constants.OBJECT, 21, 21);
        setMode(Constants.BULLET);

        this.name = name;
        this.setSpeed(16/3);
        this.posX = x;
        this.posY = y;
        this.dir = dir;
    }

    public String getName() {
        return name;
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

        drawImage();
    }

    public boolean checkPosition() {
        if(posX < 35 || posX > 735 || posY < 35 || posY > 735) {
            return true;
        }
        return false;
    }
}
