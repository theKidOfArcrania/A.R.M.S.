package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;
import armsgame.ui.test.Tools;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
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
	private DoubleProperty alph;
	private IntegerProperty energyLevel;
	private DoubleProperty energyScale;
	private IntegerProperty shieldLevel;
	private DoubleProperty shieldScale;
	private IntegerProperty weaponSets;
	private final Timeline solid = new Timeline();
	
	public static void main(String[] args) {
		launch(args);
	}

	public PlayerInfoTest() {
		sRatio = checkSmallRatio();
		alph = new SimpleDoubleProperty(.4);
		shieldLevel = new SimpleIntegerProperty(100);
		shieldScale = new SimpleDoubleProperty(1.0);
		shieldScale.bind(shieldLevel.divide(200.0));
		
		energyLevel = new SimpleIntegerProperty(50);
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
		
		Scene scene = new Scene(root, 290*wRatio, 220*hRatio);
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
		ImageView screw3 = Tools.createImageView(screw, 15, 15, 0.0, 200, sRatio, wRatio, hRatio, out);
		ImageView screw4 = Tools.createImageView(screw, 15, 15, 269, 200, sRatio, wRatio, hRatio, out);
		
		Image blueButton = Tools.createImage("BlueButton.png");
		ImageView opener = Tools.createImageView(blueButton, 60, 60, 140, 150, sRatio, wRatio, hRatio, out);
			opener.setOnMouseEntered(e->opener.setEffect(largeShade));
			opener.setOnMouseExited(e->opener.setEffect(out));
			opener.setOnMouseClicked(e->createTransparentStage(primaryStage));
		Text wDescription = Tools.createText(205, 160, wRatio, hRatio, "Weapons\nManager",  Color.web("#a9e1f7"), smallShade,Tools.createRegularFont(12, sRatio));
		
		Text name = Tools.createText(80,15,  wRatio, hRatio,"MyUsername", Color.GRAY, smallShade, Tools.createBoldFont(24,sRatio));
		//name.textProperty().bind(playerName);
		Text weapons = Tools.createText(30, 147,  wRatio, hRatio,"Weapons\n Status:", Color.GRAY, smallShade, Tools.createRegularFont(14.5,sRatio));
		Text weaponDisplay = Tools.createText(30, 182,  wRatio, hRatio,"0 Complete", Color.GRAY, smallShade, Tools.createRegularFont(12,sRatio));
		
		Text energy = Tools.createText(10, 85,  wRatio, hRatio,"Energy:", Color.rgb(116,229,135), out, Tools.createRegularFont(16,sRatio));
		Text energyDisplay = Tools.createText(135, 90,  wRatio, hRatio, "100 / 100V", Color.WHITE, out, Tools.createRegularFont(10,sRatio));	
			energyDisplay.textProperty().bind(energyLevel.asString().concat(" / 100V"));
	
		Text shield = Tools.createText(10, 117.5,  wRatio, hRatio,"Shields:", Color.LIGHTBLUE, out, Tools.createRegularFont(16,sRatio));
		Text shieldDisplay = Tools.createText(135, 122.5,  wRatio, hRatio,"0 / 200V", Color.WHITE, out, Tools.createRegularFont(10,sRatio));
			shieldDisplay.textProperty().bind(shieldLevel.asString().concat(" / 200V"));
		
		Text playerRank = Tools.createText(85.0, 57.0,  wRatio, hRatio,"Rank: ", Color.WHITE, out, Tools.createRegularFont(10,sRatio));
		Text playerGames = Tools.createText(150.0, 57.0,  wRatio, hRatio,"Games: ", Color.WHITE, out, Tools.createRegularFont(10,sRatio));
		
		Rectangle shieldEmpty = Tools.createRoundedRectangle(170,22.5,5,5,85,116.5,sRatio, wRatio, hRatio,Color.DARKGRAY.darker(), largeShade);
		Stop[] list = {new Stop(0.0, Color.WHITE),
					new Stop(0.2, Color.web("#c4e0f4")),
					new Stop(.49, Color.web("#acd5f2")), 
					new Stop(.5, Color.web("#74b2dd")), 
					new Stop(1.0, Color.web("#a9e1f7"))
					};
		LinearGradient shieldGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,list);
		Rectangle shieldBar = Tools.createRoundedRectangle((168)*shieldScale.getValue()
				,20,5,5,86,117.5,sRatio, wRatio, hRatio,Color.WHITE, null);
		shieldBar.widthProperty().bind(shieldScale.multiply(168 * wRatio));
		shieldBar.setFill(shieldGrad);
		
		Rectangle energyEmpty = Tools.createRoundedRectangle(170,22.5,5,5,85,84,sRatio, wRatio, hRatio,Color.DARKGRAY.darker(), largeShade);
		Stop[] elist = {new Stop(0.0, Color.WHITE),
					new Stop(0.3, Color.rgb(202,255,202)),
					new Stop(.49, Color.web("#acf0b7")), 
					new Stop(.5, Color.rgb(116,229,135)), 
					new Stop(1.0, Color.web("#c7f3ce"))
					};
		LinearGradient energyGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,elist);
		Rectangle energyBar = Tools.createRoundedRectangle((168)*energyScale.getValue()
				,20,5,5,86,85,sRatio, wRatio, hRatio,Color.WHITE, null);
		energyBar.widthProperty().bind(energyScale.multiply(168 * wRatio));
		energyBar.setFill(energyGrad);
		
		
		Rectangle panel = Tools.createRoundedRectangle(190,25,5,5,80,50,sRatio, wRatio, hRatio,Color.DARKGRAY.darker().darker(), largeShade);
		
		root.getChildren().addAll(name, weapons, energy,shieldEmpty, shieldBar,energyEmpty, energyBar,weaponDisplay, energyDisplay, shieldDisplay, shield,  profileView, screw1
				,screw2, screw3,screw4, panel, playerRank, playerGames, opener, wDescription);
		root.opacityProperty().bind(alph);
		
		//scene settings
		scene.setOnMouseEntered(e->trans(true));
		scene.setOnMouseExited(e->trans(false));
		root.setId("ROOTNODE");
		scene.getStylesheets().add("/armsgame/ui/test/border.css");
		
		primaryStage.setY(0);
		primaryStage.setScene(scene);
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
		
		// This is an animation test run... in the real code we will remove this.
				Transition energyAni = new Transition() {
					{
						this.setCycleDuration(Duration.seconds(3));
					}

					@Override
					protected void interpolate(double frac) {
						energyLevel.set((int) (100 - frac * 100));
					}
				};
				Transition shieldAni = new Transition() {
					{
						this.setCycleDuration(Duration.seconds(3));
					}

					@Override
					protected void interpolate(double frac) {
						shieldLevel.set((int) (200 - frac * 200));
					}
				};
				energyLevel.setValue(100);
				shieldLevel.setValue(200);

				energyAni.setDelay(shieldAni.getCycleDuration());
				shieldAni.play();
				energyAni.play();
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
	
	public void createTransparentStage(Stage primaryStage)
	{
		Stage second = new Stage();
		second.initStyle(StageStyle.TRANSPARENT);
		second.initOwner(primaryStage);
		
		AnchorPane root = new AnchorPane();
		Scene scene = new Scene(root, 1400*wRatio, 700*hRatio);
		scene.setFill(Color.rgb(100, 100, 100, .5));
		second.setScene(scene);
		second.show();
	}
}
