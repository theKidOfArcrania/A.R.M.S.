package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class PlayerInfoTest extends Application {
	private final PerspectiveCamera cameraView = new PerspectiveCamera();
	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	private final double dispWidth = displayRes.getWidth();
	private final double dispHeight = displayRes.getHeight();
	private final double wRatio = dispWidth/1600;
	private final double hRatio = dispHeight/900;
	public double sRatio;
	
	//user variables
	private SimpleIntegerProperty energy;
	private SimpleStringProperty playerName;
	private final SimpleDoubleProperty alph;
	private SimpleIntegerProperty weaponSets;
	private final Timeline solid = new Timeline();
	
	public static void main(String[] args) {
		launch(args);
	}

	public PlayerInfoTest() {
		sRatio = checkSmallRatio();
		alph = new SimpleDoubleProperty(.4);
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}
	
	private void init(Stage primaryStage) {
		
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		AnchorPane root = new AnchorPane();
		Scene scene = new Scene(root, 290*wRatio, 150*hRatio);
		scene.setFill(Color.color(.85, .85, .85,.4));
		
		InnerShadow smallShade = new InnerShadow(2.0, Color.BLACK);
		InnerShadow mediumShade = new InnerShadow(3.0, Color.BLACK);
		InnerShadow largeShade = new InnerShadow(5.0, Color.BLACK);
		InnerShadow greenShade = new InnerShadow(2.0, Color.DARKGREEN.darker());
		DropShadow out = new DropShadow(2.0, Color.BLACK);
	
		ImageView profileView = createImageView(createImage("blank-profile.jpg"), 60, 60, 10, 15, smallShade);
		Image screw = createImage("screw.png");
		ImageView screw1 = createImageView(screw, 15, 15, 0.0, 0.0, out);
		ImageView screw2 = createImageView(screw, 15, 15, scene.getWidth()-21, 0.0, out);
		ImageView screw3 = createImageView(screw, 15, 15, 0.0, scene.getHeight()-20,out);
		ImageView screw4 = createImageView(screw, 15, 15, scene.getWidth()-21, scene.getHeight()-20,out);
		
		Text name = createText(80,15, "MyUsername", Color.GRAY, smallShade, createBoldFont(24));
		//name.textProperty().bind(playerName);
		Text weapons = createText(15, 112, "Finished Weapons: ", Color.GREEN.brighter(), greenShade, createRegularFont(16));
		Text energy = createText(15, 85, "Energy: ", Color.GREEN.brighter(), greenShade, createRegularFont(16));
		Text energyDisplay = createText(85, 85, "100 Volts", Color.GRAY, smallShade, createRegularFont(16));	
		Text weaponDisplay = createText(190, 112, "0", Color.GRAY, smallShade, createRegularFont(16));
		Text playerRank = createText(85.0, 57.0, "Rank: ", Color.WHITE, out, createRegularFont(10));
		Text playerGames = createText(150.0, 57.0, "Games: ", Color.WHITE, out, createRegularFont(10));

		Rectangle panel = createRoundedRectangle(scene.getWidth()-100,25,5,5,80,50,Color.DARKGRAY.darker().darker(), largeShade);
		
		root.getChildren().addAll(name, weapons, energy,energyDisplay, weaponDisplay,  profileView, screw1
				,screw2, screw3,screw4, panel, playerRank, playerGames);
		root.opacityProperty().bind(alph);
		
		//scene settings
		scene.setOnMouseEntered(e->trans(true));
		scene.setOnMouseExited(e->trans(false));
		root.setId("ROOTNODE");
		scene.getStylesheets().add("/armsgame/ui/test/border.css");
		
		primaryStage.setY(0);
		primaryStage.setScene(scene);
		scene.setCamera(cameraView);
		primaryStage.show();



		// Timeline
		solid.getKeyFrames()
		.add(new KeyFrame(Duration.ZERO, new KeyValue(alph, .4),
				new KeyValue(scene.fillProperty(), Color.color(.85, .85, .85,.4))));

		solid.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.05), new KeyValue(alph, .6),
				new KeyValue(scene.fillProperty(), Color.color(.85, .85, .85,.6))));

		solid.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.1), new KeyValue(alph, .8),
				new KeyValue(scene.fillProperty(), Color.color(.85, .85, .85,.8))));
		solid.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.15), new KeyValue(alph, 1.0),
				new KeyValue(scene.fillProperty(), Color.color(.85, .85, .85,1.0))));
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
	
	public void trans(boolean status){
		if (solid.getStatus() == Status.RUNNING) {
			return;
		}
		if (status) {

			solid.playFromStart();
		} else {
			solid.setRate(-1);
			solid.jumpTo("end");
			solid.play();
		}

	}
	public double checkSmallRatio()
	{
	   return (wRatio>hRatio)?hRatio:wRatio;
	}
}
