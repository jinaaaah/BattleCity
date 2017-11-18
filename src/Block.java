import processing.core.PApplet;

public class Block extends RenderObject {
    public Block(PApplet pApplet) {
        super(pApplet);
        addMode(Constants.BLOCK, Constants.OBJECT, 24, 24);

        setMode(Constants.BLOCK);
    }

}
