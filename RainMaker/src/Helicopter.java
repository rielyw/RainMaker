import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.Timer;
import java.util.TimerTask;

class Helicopter extends GameObject implements Updatable {
    HelicopterState state;
    heloBody body;
    heloBlade blade;
    private Translate myTranslation;
    private Rotate myRotate;
    private GameText fuelText;
    private int fuel = 30000;

    public int getFuel() {
        return fuel;
    }

    public boolean running;
    private int fuelConsumption = 1;
    private double maxSpeed = 40;
    private double minSpeed = -2;
    private double currSpeed;
    private double heading = 0;

    public Helicopter(double centerX, double centerY) {

        body = new heloBody(centerX, centerY);

        blade = new heloBlade(centerX, centerY);

        fuelText = new GameText(fuel);
        fuelText.setColor(Color.YELLOW);
        fuelText.setTranslateX(
                centerX -
                        (fuelText.getBoundsInLocal().getWidth() / 2));
        fuelText.setTranslateY(
                centerY -
                        (fuelText.getBoundsInLocal().getHeight() / 2) - 10);

        state = new helicopterOff(this);

        getChildren().addAll(body, blade, fuelText);
        running = false;

        myTranslation = new Translate();
        myRotate = new Rotate();
        myRotate.setPivotX(getBoundsInLocal().getCenterX());
        myRotate.setPivotY(getBoundsInLocal().getCenterY());
        getTransforms().addAll(myTranslation, myRotate);

    }

    @Override
    public void update(double delta) {
        if (state instanceof helicopterReady) {
            fuel -= fuelConsumption;
            fuelText.update(fuel);
            myTranslation.setX(myTranslation.getX() + ((currSpeed * delta) *
                    Math.cos(Math.toRadians(heading + 90))));
            myTranslation.setY(myTranslation.getY() + ((currSpeed * delta) *
                    Math.sin(Math.toRadians(heading + 90))));
            myRotate.setAngle((heading) % 360);
        }

    }

    public double getSpeed() {
        return currSpeed;
    }

    public void ignitionOn() {
        running = true;
    }

    public void turnLeft() {
        heading += 15;
    }

    public void turnRight() {
        heading -= 15;
    }

    public void increaseSpeed() {
        if (currSpeed < maxSpeed) {
            currSpeed += 1;
            if ((int) Math.sqrt(Math.abs(currSpeed)) == 0) {
                fuelConsumption = 1;
            } else fuelConsumption = (int) Math.sqrt(Math.abs(currSpeed));
        }
    }

    public void decreaseSpeed() {
        if (currSpeed > -2) {
            currSpeed -= 1;
        }
    }

    public void incrementBladeSpeed() {
        blade.incrementSpeed();
    }

    public void decrementBladeSpeed() {
        blade.decrementSpeed();
    }

    public void stopBlade() {
        this.state = new helicopterOff(this);
    }


    class heloBody extends GameObject {
        ImageView heloBody;

        public heloBody(double centerX, double centerY) {
            heloBody = new ImageView("images/heloBody.png");
            heloBody.setFitWidth(68);
            heloBody.setFitHeight(92);
            heloBody.setRotate(180);
            heloBody.setX(centerX - (heloBody.getFitWidth() / 2));
            heloBody.setY(centerY - (heloBody.getFitHeight() / 2));
            getChildren().add(heloBody);
        }

    }

    class heloBlade extends GameObject {
        private Line blade;
        private Circle centerPoint;
        private double rotationalSpeed = 0;
        private Rotate myRotate;
        private AnimationTimer bladeTimer;

        public heloBlade(double centerX, double centerY) {
            blade = new Line(centerX - 45, centerY + 5,
                    centerX + 45, centerY + 5);
            blade.setStrokeWidth(4);
            blade.setStroke(Color.DARKGRAY);
            centerPoint = new Circle(centerX, centerY + 5,
                    2, Color.BLACK);
            getChildren().addAll(blade, centerPoint);

            myRotate = new Rotate();
            myRotate.setPivotX(blade.getStartX() +
                    (blade.getEndX() - blade.getStartX()) / 2);
            myRotate.setPivotY(blade.getStartY() +
                    (blade.getEndY() - blade.getStartY()) / 2);
            getTransforms().addAll(myRotate);

            bladeTimer = new AnimationTimer() {
                double old = -1;
                double delta;

                @Override
                public void handle(long now) {
                    if (old < 0) old = now;
                    delta = (now - old) / 1e9;
                    old = now;

                    myRotate.setAngle((myRotate.getAngle() + rotationalSpeed)
                            % 360);

                }
            };
            bladeTimer.start();
        }

        public void decrementSpeed() {
            if (rotationalSpeed >= 0)
                rotationalSpeed -= 1;
        }

        public void incrementSpeed() {
            if (rotationalSpeed <= 20)
                rotationalSpeed += 1;
        }

        public double getSpeed() {
            return rotationalSpeed;
        }
    }


    abstract static class HelicopterState {
        static Helicopter helicopter;

        public HelicopterState(Helicopter helo) {
            helicopter = helo;
        }

        abstract public void toggleIgnition();
    }

    static class helicopterOff extends HelicopterState {
        public helicopterOff(Helicopter helo) {
            super(helo);
        }

        public void toggleIgnition() {
            if (!helicopter.running &&
                    helicopter.intersects(Game.helipad)) {
                helicopter.ignitionOn();
            }
            helicopter.state = new helicopterStarting(helicopter);
        }
    }

    static class helicopterStarting extends HelicopterState {
        Timer timer = new Timer();

        public helicopterStarting(Helicopter helo) {
            super(helo);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    helicopter.incrementBladeSpeed();
                    if (helicopter.blade.getSpeed() == 20) {
                        helicopter.state = new helicopterReady(helicopter);
                        timer.cancel();
                    }
                }
            }, 0, 200);
        }

        public void toggleIgnition() {
            timer.cancel();
            helicopter.state = new helicopterStopping(helicopter);
        }
    }

    static class helicopterStopping extends HelicopterState {
        Timer timer = new Timer();

        public helicopterStopping(Helicopter helo) {
            super(helo);

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    helicopter.decrementBladeSpeed();
                    if (helicopter.blade.getSpeed() == 0) {
                        helicopter.state = new helicopterOff(helicopter);
                        timer.cancel();
                    }
                }
            }, 0, 200);
        }

        public void toggleIgnition() {
            timer.cancel();
            helicopter.state = new helicopterStarting(helicopter);
        }
    }

    static class helicopterReady extends HelicopterState {
        public helicopterReady(Helicopter helo) {
            super(helo);
        }

        public void toggleIgnition() {
            if (helicopter.inside(Game.helipad)
                    && helicopter.getSpeed() < 3) {
                helicopter.state = new helicopterStopping(helicopter);
            }
        }
    }
}
