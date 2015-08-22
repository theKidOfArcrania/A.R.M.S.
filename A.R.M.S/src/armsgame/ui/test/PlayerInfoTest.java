package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;
import armsgame.ui.test.Tools;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
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
	private final double wRatio = dispWidth/1600.0;
	private final double hRatio = dispHeight/900.0;
	public double sRatio;
	
	//user variables
	private IntegerProperty energy;
	private StringProperty playerName;
	private final DoubleProperty alph;
	private final IntegerProperty energyLevel;
	private final DoubleProperty energyScale;
	private IntegerProperty weaponSets;
	private final Timeline solid = new Timeline();
	
	public static void main(String[] args) {
		launch(args);
	}

	public PlayerInfoTest() {
		sRatio = checkSmallRatio();
		alph = new SimpleDoubleProperty(.4);
		energyLevel = new SimpleIntegerProperty(80);
		energyScale = new SimpleDoubleProperty(1.0);
		energyScale.bind(energyLevel.divide(100.0));
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}
	
	private void init(Stage primaryStage) {
		
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		AnchorPane root = new AnchorPane();
		Scene scene = new Scene(root, 290*wRatio, 190*hRatio);
		scene.setFill(Color.color(.85, .85, .85,.4));
		
		InnerShadow smallShade = new InnerShadow(2.0, Color.BLACK);
		InnerShadow mediumShade = new InnerShadow(3.0, Color.BLACK);
		InnerShadow largeShade = new InnerShadow(5.0, Color.BLACK);
		InnerShadow greenShade = new InnerShadow(2.0, Color.DARKGREEN.darker());
		DropShadow out = new DropShadow(2.0, Color.BLACK);
	
		ImageView profileView = Tools.createImageView(Tools.createImage("blank-profile.jpg"), 60, 60, 10, 15,  sRatio, wRatio, hRatio, smallShade);
		Image screw = Tools.createImage("screw.png");
		ImageView screw1 = Tools.createImageView(screw, 15, 15, 0.0, 0.0, sRatio, wRatio, hRatio, out);
		ImageView screw2 = Tools.createImageView(screw, 15, 15, 269, 0.0, sRatio, wRatio, hRatio, out);
		ImageView screw3 = Tools.createImageView(screw, 15, 15, 0.0, 170, sRatio, wRatio, hRatio, out);
		ImageView screw4 = Tools.createImageView(screw, 15, 15, 269, 170, sRatio, wRatio, hRatio, out);
		
		Text name = Tools.createText(80,15,  wRatio, hRatio,"MyUsername", Color.GRAY, smallShade, Tools.createBoldFont(24,sRatio));
		//name.textProperty().bind(playerName);
		Text weapons = Tools.createText(15, 150,  wRatio, hRatio,"Finished Weapons: ", Color.GREEN.brighter(), greenShade, Tools.createRegularFont(16,sRatio));
		Text weaponDisplay = Tools.createText(190, 150,  wRatio, hRatio,"0", Color.GRAY, smallShade, Tools.createRegularFont(16,sRatio));
		Text energy = Tools.createText(15, 85,  wRatio, hRatio,"Energy ", Color.GREEN.brighter(), greenShade, Tools.createRegularFont(16,sRatio));
		//Text energyDisplay = Tools.createText(85, 85,  wRatio, hRatio,"100 V", Color.GRAY, smallShade, Tools.createRegularFont(16,sRatio));	
		Text shield = Tools.createText(15, 117.5,  wRatio, hRatio,"Shields", Color.GREEN.brighter(), greenShade, Tools.createRegularFont(16,sRatio));
		//Text shieldDisplay = Tools.createText(87, 117.5,  wRatio, hRatio,"0 V", Color.GRAY, smallShade, Tools.createRegularFont(16,sRatio));
		Text playerRank = Tools.createText(85.0, 57.0,  wRatio, hRatio,"Rank: ", Color.WHITE, out, Tools.createRegularFont(10,sRatio));
		Text playerGames = Tools.createText(150.0, 57.0,  wRatio, hRatio,"Games: ", Color.WHITE, out, Tools.createRegularFont(10,sRatio));
		
		Rectangle shieldEmpty = Tools.createRoundedRectangle(170,22.5,5,5,85,116.5,sRatio, wRatio, hRatio,Color.DARKGRAY.darker(), largeShade);
		Stop[] list = {new Stop(0.0, Color.WHITE),
					new Stop(0.2, Color.web("#edf8ff")),
					new Stop(.49, Color.web("#d9f0fc")), 
					new Stop(.5, Color.web("#bee6fd")), 
					new Stop(1.0, Color.web("#a9e1f7"))
					};
		LinearGradient shieldGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,list);
		Rectangle shieldBar = Tools.createRoundedRectangle((170)*energyScale.getValue()
				,20,5,5,86,117.5,sRatio, wRatio, hRatio,Color.WHITE, null);
		shieldBar.setFill(shieldGrad);
		
		
		Rectangle panel = Tools.createRoundedRectangle(190,25,5,5,80,50,sRatio, wRatio, hRatio,Color.DARKGRAY.darker().darker(), largeShade);
		
		root.getChildren().addAll(name, weapons, energy,shieldEmpty, shieldBar,weaponDisplay, shield,  profileView, screw1
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
