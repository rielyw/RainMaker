import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

abstract class GameObject extends Group {
    // add functions to get bounds?
    // set position
    private Point2D p;
    private Translate myTranslation;
    private Rotate myRotation;
    private Scale myScale;

    public static double randNum(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static double rollD200() {
        return Math.floor(Math.random() * 200);
    }

    public static double randSpawnX() {
        double buffer = 50;
        double min = buffer;
        double max = RainMaker.GAME_WIDTH - buffer;
        return Math.random() * (max - min) + min;
    }

    public static double randSpawnY() {
        double buffer = 50;
        double min = (RainMaker.GAME_HEIGHT / 3) + buffer;
        double max = RainMaker.GAME_HEIGHT - buffer;
        return Math.random() * (max - min) + min;
    }

    public int randCircleRadius() {
        double min = 35;
        double max = 60;
        return (int) (Math.random() * (max - min) + min);
    }

    public boolean intersects(GameObject o) {
        return this.getBoundsInParent().intersects(o.getBoundsInParent());
    }

    public boolean intersects(Bounds b) {
        return this.getBoundsInParent().intersects(b);
    }

    public boolean inside(GameObject o) {
        // return true if object is fully inside o
        Bounds o1 = this.getBoundsInParent();
        Bounds o2 = o.getBoundsInParent();
        if (o1.getMinX() > o2.getMinX() &&
                o1.getMaxX() < o2.getMaxX() &&
                o1.getMinY() > o2.getMinY() &&
                o1.getMaxY() < o2.getMaxY()) {
            return true;
        }
        return false;
    }

}
