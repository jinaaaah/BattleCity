import processing.core.PApplet;

public class Program extends PApplet{
    private ResourceManger resourceManger;

    @Override
    public void settings() {
        size(500,500);
        resourceManger = new ResourceManger();
    }

    @Override
    public void draw() {
        background(0);
    }

    @Override
    public void setup() {
        background(0);
    }
    public static void main(String[] args) {
        PApplet.main("Program");
    }
}
