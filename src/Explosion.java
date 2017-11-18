import processing.core.PApplet;

public class Explosion extends RenderObject{
    protected long makeTime;
    protected boolean removeOk = false;

    public Explosion(PApplet pApplet, int x, int y) {
        super(pApplet);
        addMode(Constants.EXPLOSION, Constants.OBJECT, 17, 19);
        setMode(Constants.EXPLOSION);
        makeTime = System.currentTimeMillis();
        posX = x;
        posY = y;
    }

    public boolean isRemoveOk() {
        return removeOk;
    }

    @Override
    public void update() {
        if(System.currentTimeMillis() - makeTime > 300) {
            removeOk = true;
        }
    }

    @Override
    public void render() {
        tick++;
        pApplet.image(pImages.get(tick/8%pImages.size()), posX, posY);
    }
}
