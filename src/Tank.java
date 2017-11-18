import processing.core.PApplet;
import processing.core.PConstants;

public class Tank extends RenderObject {
    protected int team;
    public Tank(PApplet pApplet) {
        super(pApplet);
        this.setSpeed(1);
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

    }

    @Override
    public void render() {
        if(isMoved) {
            tick++;
        }
        pImage = pImages.get((tick/8) % pImages.size());
        pApplet.imageMode(PConstants.CENTER);
        pApplet.translate(18,20);
        pApplet.pushMatrix();
        if (this.dir == Constants.MOVE_LEFT) {
            pApplet.rotate(PApplet.radians(270.0f));
            pApplet.image(pImage, -posY, posX);
        } else if (this.dir == Constants.MOVE_RIGHT) {
            pApplet.rotate(PApplet.radians(90.0f));
            pApplet.image(pImage, posY, -posX);
        } else if (this.dir == Constants.MOVE_DOWN) {
            pApplet.rotate(PApplet.radians(180.0f));
            pApplet.image(pImage, -posX, -posY);
        } else {
            pApplet.image(pImage, posX, posY);
        }

        pApplet.popMatrix();


        /*if(this.dir == Constants.STOP){
            pImage = pImages.get(0);
            pApplet.translate(18,20);
            pApplet.image(pImage, posX, posY);

        }else{
            tick++;
            pImage = pImages.get((tick/8) % pImages.size());
            pApplet.imageMode(PConstants.CENTER);
            pApplet.translate(18,20);
            pApplet.pushMatrix();
            if (this.dir == Constants.MOVE_LEFT) {
                pApplet.rotate(PApplet.radians(270.0f));
                pApplet.image(pImage, posY, posX);
            } else if (this.dir == Constants.MOVE_RIGHT) {
                pApplet.rotate(PApplet.radians(90.0f));
                pApplet.image(pImage, posY, posX);
            } else if (this.dir == Constants.MOVE_DOWN) {
                pApplet.rotate(PApplet.radians(180.0f));
                pApplet.image(pImage, posX, posY);
            } else {
                pApplet.image(pImage, posX, posY);
            }

            pApplet.popMatrix();

        }*/


    }
}
