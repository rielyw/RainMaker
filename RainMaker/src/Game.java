import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;

import java.util.Iterator;

class Game extends Pane {
    private DesertBackground background;
    public static Helipad helipad;
    public static Helicopter helicopter;
    public static CloudsAndPonds cloudsandponds;
    public static Bounds GameBounds;


    public Game() {
        setWidth(RainMaker.GAME_WIDTH);
        setHeight(RainMaker.GAME_HEIGHT);
        GameBounds = new BoundingBox(0,0,
                this.getWidth(),this.getHeight());
        background = new DesertBackground();
        setScaleY(-1);
        helipad = new Helipad();
        makeHelicopter();
        cloudsandponds = new CloudsAndPonds();

        getChildren().addAll(background,
                cloudsandponds, helipad, helicopter);
    }

    private void makeHelicopter() {
        helicopter = new Helicopter(
                helipad.getBoundsInParent().getMinX() +
                        (helipad.getBoundsInParent().getWidth()/2),
                helipad.getBoundsInParent().getMinY() +
                        (helipad.getBoundsInParent().getHeight()/2)
        );
    }

    public void restartGame() {
        getChildren().clear();
        background = new DesertBackground();
        helipad = new Helipad();
        helicopter = new Helicopter(
                helipad.getBoundsInParent().getMinX() +
                        (helipad.getBoundsInParent().getWidth()/2),
                helipad.getBoundsInParent().getMinY() +
                        (helipad.getBoundsInParent().getHeight()/2)
        );
        cloudsandponds = new CloudsAndPonds();

        getChildren().addAll(background, cloudsandponds, helipad, helicopter);
    }

    public void update(double delta, AnimationTimer gameTimer) {
        helicopter.update(delta);
        cloudsandponds.update(delta);
        handleWinConditions(gameTimer);
        handleLoseConditions(gameTimer);
    }

    private void handleWinConditions(AnimationTimer gameTimer) {
        if (cloudsandponds.allPondsFull()) {
            helicopter.stopBlade();
            gameTimer.stop();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "You Won! Do you want to play again?",
                    ButtonType.YES, ButtonType.NO);
            alert.setOnHidden(e -> {
                if (alert.getResult() == ButtonType.YES) {
                    restartGame();
                    gameTimer.start();
                }
                else {
                    Platform.exit();
                    System.exit(0);                }
            });
            alert.show();
        }
    }

    private void handleLoseConditions(AnimationTimer gameTimer) {
        if (helicopter.getFuel() <= 0) {
            helicopter.stopBlade();
            gameTimer.stop();
            int score = cloudsandponds.getScore();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "You Lost. Score " + score +
                            ". Do you want to play again?",
                    ButtonType.YES, ButtonType.NO);
            alert.setOnHidden(e -> {
                if (alert.getResult() == ButtonType.YES) {
                    restartGame();
                    gameTimer.start();
                }
                else {
                    Platform.exit();
                    System.exit(0);                }
            });
            alert.show();
        }
    }

    public void toggleHelicopterIgnition() {
        helicopter.state.toggleIgnition();
    }

    public void leftArrowKey() {
        if (helicopter.state instanceof Helicopter.helicopterReady) {
            helicopter.turnLeft();
        }
    }
    public void rightArrowKey() {
        if (helicopter.state instanceof Helicopter.helicopterReady) {
            helicopter.turnRight();
        }
    }
    public void upArrowKey() {
        if (helicopter.state instanceof Helicopter.helicopterReady) {
            helicopter.increaseSpeed();
        }
    }
    public void downArrowKey() {
        if (helicopter.state instanceof Helicopter.helicopterReady) {
            helicopter.decreaseSpeed();
        }
    }

    public void spaceKey() {
        Iterator itr = Game.cloudsandponds.clouds.iterator();
        while (itr.hasNext()) {
            Object o = itr.next();
            if (o instanceof Cloud cloud) {
                if (cloud.intersects(Game.helicopter)) {
                    Game.cloudsandponds.seedCloud(cloud);
                }
            }
        }
    }
}

