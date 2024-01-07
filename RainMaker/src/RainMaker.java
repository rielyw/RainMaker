import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;

public class RainMaker extends Application {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 800;
    public static final int WIND_SPEED = 1;
    public static final int WIND_DIRECTION = 90;
    private static Set<KeyCode> keysDown = new HashSet<>();

    public static void main(String[] args) {
        launch(args);
    }

    public Game rainMaker;
    public AnimationTimer gameTimer;

    public void start(Stage stage) {
        rainMaker = new Game();
        Scene scene = new Scene(rainMaker);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Rain Maker");

        gameTimer = new AnimationTimer() {
            double old = -1;
            double delta;
            double delay = 0;
            @Override
            public void handle(long now) {
                if (old < 0) old = now;
                delta = (now - old) / 1e9;
                old = now;
                delay++;

                rainMaker.update(delta,gameTimer);

                if (delay == 3) {
                    delay = 0;
                    executeKeyCommands();
                }

            }
        };

        scene.setOnKeyPressed(e -> {keyPressed(e);});
        scene.setOnKeyReleased(e -> {keyReleased(e);});

        gameTimer.start();
        stage.show();
    }


    public void executeKeyCommands() {
        for (KeyCode k : keysDown) {
            if (k == KeyCode.R) {
                rainMaker.restartGame();
            }
            else if (k == KeyCode.LEFT) {
                rainMaker.leftArrowKey();
            }
            else if (k == KeyCode.RIGHT) {
                rainMaker.rightArrowKey();
            }
            else if (k == KeyCode.UP) {
                rainMaker.upArrowKey();
            }
            else if (k == KeyCode.DOWN) {
                rainMaker.downArrowKey();
            }
            else if (k == KeyCode.SPACE) {
                rainMaker.spaceKey();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.I) {     // special case for I
            rainMaker.toggleHelicopterIgnition();
        }
        if (e.getCode() == KeyCode.R) {
            keysDown.add(e.getCode());
        }
        if (e.getCode() == KeyCode.LEFT) {
            keysDown.add(e.getCode());
        }
        if (e.getCode() == KeyCode.RIGHT) {
            keysDown.add(e.getCode());
        }
        if (e.getCode() == KeyCode.UP) {
            keysDown.add(e.getCode());
        }
        if (e.getCode() == KeyCode.DOWN) {
            keysDown.add(e.getCode());
        }
        if (e.getCode() == KeyCode.SPACE) {
            keysDown.add(e.getCode());
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getCode() == KeyCode.R) {
            keysDown.remove(e.getCode());
        }
        if (e.getCode() == KeyCode.LEFT) {
            keysDown.remove(e.getCode());
        }
        if (e.getCode() == KeyCode.RIGHT) {
            keysDown.remove(e.getCode());
        }
        if (e.getCode() == KeyCode.UP) {
            keysDown.remove(e.getCode());
        }
        if (e.getCode() == KeyCode.DOWN) {
            keysDown.remove(e.getCode());
        }
        if (e.getCode() == KeyCode.SPACE) {
            keysDown.remove(e.getCode());
        }
    }
}