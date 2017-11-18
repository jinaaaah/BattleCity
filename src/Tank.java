import processing.core.PApplet;
import processing.core.PConstants;

public class Tank extends RenderObject {
    protected int team;
    protected Bullet bullet;
    protected boolean isDead;

    public Tank(PApplet pApplet) {
        super(pApplet);
        this.setSpeed(2);
        isDead = false;
        this.dir = Constants.STOP;
        addMode(Constants.A_TEAM, Constants.OBJECT, 1, 8);
        addMode(Constants.B_TEAM, Constants.OBJECT, 9, 16);

        setMode(Constants.A_TEAM);
    }

    @Override
    public void update() {
        if(isMoved) {
            if (this.dir == Constants.MOVE_LEFT) {
                posX -= speed;
            } else if (this.dir == Constants.MOVE_RIGHT) {
                posX += speed;
            } else if (this.dir == Constants.MOVE_UP) {
                posY -= speed;
            } else if (this.dir == Constants.MOVE_DOWN) {
                posY += speed;
            }
            System.out.println(dir + ":" + posX + "," + posY);
        }
        reload();
    }

    @Override
    public void render() {
        if(isMoved) {
            tick++;
        }
        pImage = pImages.get((tick/8) % pImages.size());
        pApplet.imageMode(PConstants.CENTER);
        pApplet.translate(18,20);

        drawImage();
    }

    public Bullet shoot() {
        if(bullet == null) {
            bullet = new Bullet(pApplet, posX, posY, dir);
        }
        return bullet;
    }

    public void reload() {
        if(bullet != null && bullet.checkPosition()) {
            bullet = null;
        }
    }
}
