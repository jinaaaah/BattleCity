import processing.core.PApplet;

public class Block extends RenderObject {
    public Block(PApplet pApplet) {
        super(pApplet);
        addMode(Constants.BLOCK, Constants.OBJECT, 24, 24);

        setMode(Constants.BLOCK);
    }
    @Override
    public boolean checkCollision(int pX, int pY) {
        return (pX + 20 > this.posX && pX < this.posX + 20
                && pY + 20 > this.posY && pY < this.posY + 20);
    }
}
