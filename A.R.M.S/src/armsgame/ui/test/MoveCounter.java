package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class MoveCounter extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	private final PerspectiveCamera cameraView = new PerspectiveCamera();
	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	private final double dispWidth = displayRes.getWidth();
	private final double dispHeight = displayRes.getHeight();
	private final double wRatio = dispWidth/1600;
	private final double hRatio = dispHeight/900;
	public double sRatio;
	
	private static int count;
	private SimpleDoubleProperty alph;
	private final Timeline solid = new Timeline();
	public ArrayList<ImageView> lightList;
	
	public MoveCounter() {
		alph = new SimpleDoubleProperty(1.0);
		sRatio = checkSmallRatio();
		lightList = new ArrayList<ImageView>();
		count = 3;
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}

	private void init(Stage primaryStage) {
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		AnchorPane root = new AnchorPane();
		Scene scene = new Scene(root, 267*wRatio, 225*hRatio);
		InnerShadow smallShade = new InnerShadow(2.0, Color.BLACK);
		InnerShadow mediumShade = new InnerShadow(3.0, Color.BLACK);
		InnerShadow largeShade = new InnerShadow(5.0, Color.BLACK);
		DropShadow out = new DropShadow(2.0, Color.BLACK);
		
		Rectangle panel = createRoundedRectangle(217,140, 60, 60, 22, 52, Color.DARKGRAY.darker().darker(), largeShade);
		Text name = createText(0, 12, "Move Counter", Color.GRAY, smallShade, createBoldFont(28));
		name.setWrappingWidth(dispWidth/6);
		name.setTextAlignment(TextAlignment.CENTER);
		
        Image lightoff = createImage("lightoff.png");
        Image light = createImage("Greenlight.png");
        Image screw = createImage("screw.png");
        ImageView light1= createImageView(light, 56, 56, 36, 91.5, mediumShade); lightList.add(light1);
        ImageView light2= createImageView(light, 56, 56, 102.5, 91.5, mediumShade); lightList.add(light2);
        ImageView light3= createImageView(light, 56, 56, 168, 91.5, mediumShade); lightList.add(light3);
		ImageView light1off = createImageView(lightoff, 56, 56, 36, 91.5, largeShade);
		ImageView light2off = createImageView(lightoff, 56, 56, 102.5, 91.5, largeShade);
		ImageView light3off = createImageView(lightoff, 56, 56, 168, 91.5, largeShade);
		ImageView screw1 = createImageView(screw, 15, 15, 0.0, 0.0, out);
		ImageView screw2 = createImageView(screw, 15, 15, scene.getWidth()-21, 0.0, out);
		ImageView screw3 = createImageView(screw, 15, 15, 0.0, scene.getHeight()-20, out);
		ImageView screw4 = createImageView(screw, 15, 15, scene.getWidth()-21, scene.getHeight()-20, out);
        
        
		root.getChildren().addAll(name, panel, light1off, light2off, light3off, light1, 
				light2, light3,screw1,screw2,screw3,screw4);
		root.setOnMouseClicked(e->lightOff(lightList));
   
		
		root.setId("ROOTNODE");
		scene.getStylesheets().add("/armsgame/ui/test/border.css");
		primaryStage.setX(0);
		primaryStage.setY(dispHeight - scene.getHeight());

		primaryStage.setScene(scene);
		scene.setCamera(cameraView);
		primaryStage.show();

	}
	public void lightOff(ArrayList<ImageView> lightList)
	{
		count--;
		if(count<0)
		{
			resetMoves(lightList);
		}
		else
		{
			genAnimation(lightList.get(count));
		}
	}
	public void genAnimation(ImageView light)
	{
		
		Timeline anim = new Timeline();
		anim.getKeyFrames()
		.add(new KeyFrame(Duration.ZERO, new KeyValue(light.opacityProperty(), 1.0)));
		
		anim.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.05), new KeyValue(light.opacityProperty(), .75)));
		
		anim.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.1), new KeyValue(light.opacityProperty(), .5)));
		
		anim.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.15), new KeyValue(light.opacityProperty(), .25)));
		
		anim.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.2), new KeyValue(light.opacityProperty(), 0)));
		
		anim.playFromStart();
		
	}
	
	public void resetMoves(ArrayList<ImageView> lightList)
	{
		count = lightList.size();
		for(ImageView light: lightList)
		{
			light.setOpacity(1.0);
		}
	}
	public Rectangle createRoundedRectangle(double w, double h, double xR, double yR, double x, double y, Color c, Effect g)
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
	public Font createRegularFont(double size)
	{
		return Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Regular.otf").toExternalForm(), size*sRatio);
	}
	public Font createBoldFont(double size)
	{
		return Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Bold.otf").toExternalForm(), size*sRatio);
	}
	public Text createText(double x, double y, String text, Color color, Effect g, Font f)
	{
		Text t = new Text(text);
		t.setFill(color);
		t.setFont(f);
		AnchorPane.setTopAnchor(t, y*hRatio);
		AnchorPane.setLeftAnchor(t,x*wRatio);
		t.setEffect(g);
		
		return t;
	}
	public Image createImage(String location)
	{
		return new Image(PlayerInfoTest.class.getResource(location).toExternalForm());
	}
	public ImageView createImageView(Image img, double h, double w, double x, double y, Effect g)
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
	public double checkSmallRatio()
	{
	   return (wRatio>hRatio)?hRatio:wRatio;
	}
}