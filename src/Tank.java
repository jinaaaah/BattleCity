import processing.core.PApplet;

public class Tank extends RenderObject {
    protected int team;
    protected String name;
    protected boolean isDead;

    public Tank(PApplet pApplet) {
        super(pApplet);
        this.setSpeed(2);
        isDead = false;
        this.dir = Constants.STOP;
        addMode(Constants.A_TEAM, Constants.OBJECT, 1, 8);
        addMode(Constants.B_TEAM, Constants.OBJECT, 9, 16);

    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setTeam(int team) {
        setMode(team);
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
    }

    @Override
    public void render() {
        if(isMoved) {
            tick++;
        }
        pImage = pImages.get((tick/8) % pImages.size());
        drawImage();
    }

}
