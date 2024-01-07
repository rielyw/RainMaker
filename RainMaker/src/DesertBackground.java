import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

class DesertBackground extends Pane {
    private ImageView backgroundImage;

    public DesertBackground() {
        backgroundImage = new ImageView("images/background.jpg");
        backgroundImage.setFitWidth(800);
        backgroundImage.setFitHeight(800);
        getChildren().add(backgroundImage);
    }
}
