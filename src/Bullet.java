import processing.core.PApplet;

public class Bullet extends RenderObject {
    private Tank tank;
    private boolean isDead;

    public Bullet(PApplet pApplet) {
        super(pApplet);
        addMode(Constants.BULLET, Constants.OBJECT, 21, 21);
        addMode(Constants.EXPLOSION, Constants.OBJECT, 17, 19);

        setMode(Constants.BULLET);
        this.setSpeed(1);
        isDead = false;
    }
    public void setTank(Tank tank){
        this.tank = tank;
        this.dir = tank.getDir();
        this.posX = tank.getPosX();
        this.posY = tank.getPosY();
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

}
