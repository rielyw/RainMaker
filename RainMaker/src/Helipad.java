import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

class Helipad extends GameObject {
    static private int HELIPAD_WIDTH = 110;
    static private int HELIPAD_HEIGHT = 110;
    private Circle c;
    private Rectangle r;
    private GameText letter;

    public Helipad() {
        r = new Rectangle((RainMaker.GAME_WIDTH / 2) - (HELIPAD_WIDTH / 2),
                (RainMaker.GAME_HEIGHT / 6) - (HELIPAD_HEIGHT / 2),
                HELIPAD_WIDTH, HELIPAD_HEIGHT);
        r.setFill(Color.GREY);
        r.setStroke(Color.WHITE);
        c = new Circle(
                (r.getBoundsInParent().getMinX()) + (r.getWidth() / 2),
                (r.getBoundsInParent().getMinY()) + (r.getHeight() / 2),
                40, Color.TRANSPARENT);
        c.setStroke(Color.TRANSPARENT);
        letter = new GameText("H");
        letter.setColor(Color.WHITE);
        letter.setFont(new Font("Helvetica", 100));
        letter.setTranslateX(
                c.getCenterX() -
                        (letter.getLayoutBounds().getWidth() / 2));
        letter.setTranslateY(
                c.getCenterY() +
                        (letter.getLayoutBounds().getHeight() / 2) - 30);


        getChildren().addAll(r,c,letter);
    }

}
