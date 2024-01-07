import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

class GameText extends GameObject {
    Text text;

    public GameText(String TextString) {
        text = new Text(TextString);
        text.setScaleY(-1);
        getChildren().add(text);
    }

    public GameText(int TextInt) {
        text = new Text(Integer.toString(TextInt));
        text.setScaleY(-1);
        getChildren().add(text);
    }

    public void update(int TextInt) {
        text.setText(Integer.toString(TextInt));
    }

    public void update(String TextString) {
        text.setText(TextString);
    }

    public void setColor(Color color) {
        text.setFill(color);
    }

    public void setFont(Font font) {text.setFont(font);}
}
