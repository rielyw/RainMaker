import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

import java.util.Timer;
import java.util.TimerTask;

class Cloud extends GameObject implements Updatable {
    CloudState state;
    private Circle c;
    private GameText percentText;
    private int percent = 0;

    public int getPercent() {
        return percent;
    }

    private Color saturation = Color.rgb(255, 255, 255);
    private double minSaturationColor = 255;
    private double minSaturation = 0;
    private double maxSaturation = 100;
    private Translate myTranslation;
    private final double heading = RainMaker.WIND_DIRECTION;
    private final double speed = RainMaker.WIND_SPEED;
    private final double speedModifier = GameObject.randNum(5, 10);
    private int updateCallCounter = 0;

    public Cloud() {
        super();
        state = new CloudCreated(this);
        c = new Circle(randSpawnX(), randSpawnY(),
                randCircleRadius());
        c.setStroke(Color.BLACK);
        c.setFill(saturation);

        percentText = new GameText("0%");
        percentText.setColor(Color.BLACK);
        percentText.setTranslateX(
                c.getCenterX() -
                        (percentText.getLayoutBounds().getWidth() / 2));
        percentText.setTranslateY(
                c.getCenterY() +
                        (percentText.getLayoutBounds().getHeight() / 2));

        getChildren().addAll(c, percentText);

        myTranslation = new Translate();
        getTransforms().addAll(myTranslation);
    }

    public Cloud(double x, double y) {
        super();
        state = new CloudCreated(this);
        c = new Circle(x, y,
                randCircleRadius());
        c.setStroke(Color.BLACK);
        c.setFill(saturation);

        percentText = new GameText("0%");
        percentText.setColor(Color.BLACK);
        percentText.setTranslateX(
                c.getCenterX() -
                        (percentText.getLayoutBounds().getWidth() / 2));
        percentText.setTranslateY(
                c.getCenterY() +
                        (percentText.getLayoutBounds().getHeight() / 2));

        getChildren().addAll(c, percentText);

        myTranslation = new Translate();
        getTransforms().addAll(myTranslation);
    }

    public void update(double delta) {
        move(delta);
        updateSaturation();
        percentText.update(percent + "%");
        saturation = Color.rgb((int) (minSaturationColor - percent),
                (int) (minSaturationColor - percent),
                (int) (minSaturationColor - percent));
        c.setFill(saturation);
    }

    private void move(double delta) {
        myTranslation.setX(myTranslation.getX() +
                (((speed + speedModifier) * delta) *
                        Math.cos(Math.toRadians(heading - 90))));
        myTranslation.setY(myTranslation.getY() +
                (((speed + speedModifier) * delta) *
                        Math.sin(Math.toRadians(heading - 90))));
    }

    private void updateSaturation() {
        updateCallCounter++;
        if ((updateCallCounter % 40 == 0) && (percent > 0)) {
            updateCallCounter = 0;
            decreaseSaturation();
        }
    }

    public void increaseSaturation() {
        if (percent < maxSaturation) percent++;
    }

    public void decreaseSaturation() {
        if (percent > minSaturation) percent--;
    }


    abstract static class CloudState {
        protected Cloud cloud;

        public CloudState(Cloud c) {
            cloud = c;
        }

        abstract public void nextState();
    }

    static class CloudCreated extends CloudState {
        Timer timer = new Timer();

        public CloudCreated(Cloud c) {
            super(c);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (cloud.intersects(Game.GameBounds)) {
                        nextState();
                        timer.cancel();
                    }
                }
            }, 0, 500);
        }

        public void nextState() {
            cloud.state = new CloudOnScreen(cloud);
        }
    }

    static class CloudOnScreen extends CloudState {
        Timer timer = new Timer();

        public CloudOnScreen(Cloud c) {
            super(c);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (!(cloud.intersects(Game.GameBounds))) {
                        nextState();
                        timer.cancel();
                    }
                }
            }, 0, 500);
        }

        public void nextState() {
            cloud.state = new CloudDead(cloud);
        }
    }

    static class CloudDead extends CloudState {
        public CloudDead(Cloud c) {
            super(c);
        }

        public void nextState() {
        }
    }
}
