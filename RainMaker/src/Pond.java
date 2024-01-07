import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class Pond extends GameObject {
    private Circle c;
    private Point2D center;
    private GameText percentText;
    private double percent = (Math.random() * (32 - 18) + 18);
    public double getPercent() { return percent; }
    private int maxRainCollectionDistance;
    private double area;
    private int diameter;
    public int getDiameter() { return diameter; }

    public Pond() {
        super();
        c = new Circle(randSpawnX(), randSpawnY(),
                randCircleRadius(), Color.DARKBLUE);
        diameter = (int)c.getRadius() * 2;
        area = Math.PI * Math.pow(c.getRadius(), 2);
        c.setStroke(Color.BLUE);

        percentText = new GameText((int)percent + "%");
        percentText.setColor(Color.WHITE);
        percentText.setTranslateX(
                c.getCenterX() -
                        (percentText.getLayoutBounds().getWidth() / 2));
        percentText.setTranslateY(
                c.getCenterY() +
                        (percentText.getLayoutBounds().getHeight() / 2));

        getChildren().addAll(c, percentText);
    }

    public void update(double distance) {
        if (percent < 100) {
            if (percent + (( (4*diameter) / distance ) / 2) > 100)
                percent = 100;
            else
                percent = percent + (( (4*diameter) / distance ) / 2);
            area += 45;
            percentText.update((int)percent + "%");
            c.setRadius(Math.sqrt(area / Math.PI));
            diameter = (int) c.getRadius() * 2;
        }
    }

}
