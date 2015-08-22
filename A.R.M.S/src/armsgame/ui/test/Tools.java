package armsgame.ui.test;

import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tools {
	public static Rectangle createRoundedRectangle(double w, double h, double xR, double yR, double x, double y, 
			double sRatio, double wRatio, double hRatio, Color c, Effect g)
	{
		Rectangle r = new Rectangle(w*wRatio,h*hRatio);
		r.setArcHeight(yR);
		r.setArcWidth(xR);
		AnchorPane.setTopAnchor(r, y);
		AnchorPane.setLeftAnchor(r,x);
		r.setFill(c);
		r.setEffect(g);
		
		return r;
	}
	public static Font createRegularFont(double size, double sRatio)
	{
		return Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Regular.otf").toExternalForm(), size*sRatio);
	}
	public static Font createBoldFont(double size, double sRatio)
	{
		return Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Bold.otf").toExternalForm(), size*sRatio);
	}
	public static Text createText(double x, double y, double wRatio, double hRatio,
			String text, Color color, Effect g, Font f)
	{
		Text t = new Text(text);
		t.setFill(color);
		t.setFont(f);
		AnchorPane.setTopAnchor(t, y*hRatio);
		AnchorPane.setLeftAnchor(t,x*wRatio);
		t.setEffect(g);
		
		return t;
	}
	public static Image createImage(String location)
	{
		return new Image(PlayerInfoTest.class.getResource(location).toExternalForm());
	}
	public static ImageView createImageView(Image img, double h, double w, double x, double y, 
			double sRatio, double wRatio, double hRatio, Effect g)
	{
		ImageView sv = new ImageView();
		sv.setImage(img);
		sv.setFitHeight(h*sRatio);
		sv.setFitWidth(w*sRatio);
		sv.setPreserveRatio(true);
		sv.setSmooth(true);
		sv.setCache(true);
		sv.setEffect(g);
		AnchorPane.setTopAnchor(sv, y*hRatio);
		AnchorPane.setLeftAnchor(sv, x*wRatio);
		
		return sv;
	}
}
