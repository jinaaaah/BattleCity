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

    public void setTeam(int team) {
        this.team = team;
    }

    @Override
    public boolean checkCollision(int pX, int pY) {
        int dx = posX;
        int dy = posY;
        if (this.dir == Constants.MOVE_LEFT) {
            dx = posX - speed -1;
        } else if (this.dir == Constants.MOVE_RIGHT) {
            dx = posX + speed +1;
        } else if (this.dir == Constants.MOVE_UP) {
            dy = posY - speed -1;
        } else if (this.dir == Constants.MOVE_DOWN) {
            dy = posY + speed +1;
        }

//        if(dx + 40 > pX ){
//            posY -= speed;
//        }

//        if(pX + 40 > this.posX){
//            this.posX
//        }
//        return (pX + 40 > this.posX && pX < this.posX + 40
//                && pY + 40 > this.posY && pY < this.posY + 40);

        return (dx + 40 > pX && dx < pX + 40
                && dy + 40 > pY && dy < pY + 40);
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
        }
        reload();
    }

    @Override
    public void render() {
        if(isMoved) {
            tick++;
        }
        pImage = pImages.get((tick/8) % pImages.size());
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
