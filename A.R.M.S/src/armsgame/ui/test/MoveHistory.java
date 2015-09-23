package armsgame.ui.test;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.util.ArrayList;

import armsgame.res.Tools;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.Animation.Status;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
//
public class MoveHistory extends Application{
	
	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	private final double dispWidth = displayRes.getWidth();
	private final double dispHeight = displayRes.getHeight();
	private final double wRatio = dispWidth/1600.0;
	private final double hRatio = dispHeight/900.0;
	private static int moveCount = 1;
	public double sRatio;
	public double[] positions = {420,556.25,692.5,828.75,965,1101.25,1237.5,1373.75};
	
	public static void main(String[] args) {
		launch(args);
	}

	public MoveHistory() {
		sRatio = checkSmallRatio();
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		AnchorPane pane = new AnchorPane();
		//Test for the Settings button, to be implemented in the main UI class
		Image setting = Tools.createImage("settings.png");
		ImageView settingView = Tools.createImageView(setting, 95, 95, dispWidth-95, 15, sRatio, wRatio, hRatio, new InnerShadow(5.0, Color.BLACK));
		pane.getChildren().add(settingView);
		Scene trans = new Scene(pane, dispWidth, dispHeight);
				trans.setFill(Color.TRANSPARENT);
				
				
		primaryStage.setScene(trans);
		primaryStage.show();
		Stage s = initMain("dirtymetal3.png", 290*wRatio);
		s.initOwner(primaryStage);
		s.show();
		ArrayList<Stage> moves = new ArrayList<Stage>();
		
		//test
		Stage part1 = moveInit("Turn "+moveCount, "blank-profile.jpg","blank-profile.jpg", 0);
		part1.initOwner(primaryStage);
		part1.show();
		moveCount++;
		Stage part2 = moveInit("Turn "+moveCount, "blank-profile.jpg","blank-profile.jpg", 1);
		part2.initOwner(primaryStage);
		part2.show();
		moveCount++;
		Stage part3 = moveInit("Turn "+moveCount, "blank-profile.jpg","blank-profile.jpg", 2);
		part3.initOwner(primaryStage);
		part3.show();
		moveCount++;
		Stage part4 = moveInit("Turn "+moveCount, "blank-profile.jpg","blank-profile.jpg", 3);
		part4.initOwner(primaryStage);
		part4.show();
		moveCount++;
		Stage part5 = moveInit("Turn "+moveCount, "blank-profile.jpg","blank-profile.jpg", 4);
		part5.initOwner(primaryStage);
		part5.show();
		moveCount++;
		Stage part6 = moveInit("Turn "+moveCount, "blank-profile.jpg","blank-profile.jpg", 5);
		part6.initOwner(primaryStage);
		part6.show();
		moveCount++;
		Stage part7 = moveInit("Turn "+moveCount, "blank-profile.jpg","blank-profile.jpg", 6);
		part7.initOwner(primaryStage);
		part7.show();
		moveCount++;
		Stage part8 = moveInit("Turn "+moveCount, "blank-profile.jpg","blank-profile.jpg", 7);
		part8.initOwner(primaryStage);
		part8.show();
		
	}
	private Stage moveInit(String string, String image, String playerImage, int position)
	{
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		AnchorPane root = new AnchorPane();
		
		Scene scene = new Scene(root, 134*wRatio, 80*hRatio);
		scene.setFill(Color.color(.85, .85, .85,.4));
		double w = scene.getWidth();
		double h = scene.getHeight();
		InnerShadow smallShade = new InnerShadow(2.0, Color.BLACK);
		InnerShadow mediumShade = new InnerShadow(3.0, Color.BLACK);
		InnerShadow largeShade = new InnerShadow(5.0, Color.BLACK);
		InnerShadow whiteShade = new InnerShadow(3.0, Color.WHITE);
		
		Text name = Tools.createText(5,10,  wRatio, hRatio, string, Color.GRAY.darker(), smallShade, Tools.createBoldFont(14,sRatio));
		Image cardIcon= Tools.createImage(image);
		ImageView cardIconView = Tools.createImageView(cardIcon, 60, 60, w-70, h-70, sRatio, wRatio, hRatio, smallShade);
		Image playerIcon= Tools.createImage(image);
		ImageView playerIconView = Tools.createImageView(playerIcon, 40, 40, 15, h-50, sRatio, wRatio, hRatio, smallShade);
		root.getChildren().addAll(name, cardIconView, playerIconView);
		
		
		Image background = Tools.createImage("dirtymetal4.jpg");
		BackgroundImage back = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
	               new BackgroundSize(scene.getWidth(), scene.getHeight(), false, false, false, false));
		root.setBackground(new Background(back));
		root.setEffect(largeShade);
		
		root.setOnMouseEntered(e -> root.setEffect(whiteShade));
		root.setOnMouseExited(e -> root.setEffect(largeShade));
		root.setOnMouseClicked(e -> createTransparentStage(primaryStage,positions[position]*wRatio));
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setY(5);
		primaryStage.setX(positions[position]*wRatio);
		primaryStage.setScene(scene);
		
		return primaryStage;
	}
	private Stage initMain(String image, double x) {
		
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		AnchorPane root = new AnchorPane();
		
		Scene scene = new Scene(root, 1220*wRatio, 90*hRatio);
		scene.setFill(Color.color(.85, .85, .85,.4));
		double w = scene.getWidth();
		double h = scene.getHeight();
		InnerShadow smallShade = new InnerShadow(2.0, Color.BLACK);
		InnerShadow mediumShade = new InnerShadow(3.0, Color.BLACK);
		InnerShadow largeShade = new InnerShadow(5.0, Color.BLACK);
		InnerShadow greenShade = new InnerShadow(2.0, Color.DARKGREEN.darker());
		DropShadow out = new DropShadow(2.0, Color.BLACK);

		Text name = Tools.createText(13,15,  wRatio, hRatio," Move\nHistory", Color.GRAY.darker(), smallShade, Tools.createBoldFont(24,sRatio));
		Rectangle panel = Tools.createRoundedRectangle(4,90,2,2,122,0,sRatio, wRatio, hRatio, Color.GRAY.darker(), mediumShade);
		
		root.getChildren().addAll(name,panel);
		

		 Image background = Tools.createImage(image);
		 BackgroundImage back = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
	               new BackgroundSize(scene.getWidth(), scene.getHeight(), false, false, false, false));
		root.setBackground(new Background(back));
		root.setEffect(mediumShade);
		primaryStage.setY(0);
		primaryStage.setX(x*wRatio);
		primaryStage.setScene(scene);
		
		return primaryStage;
				
	}

	
	public double checkSmallRatio()
	{
	   return (wRatio>hRatio)?hRatio:wRatio;
	}
	public void createMoveCounter(Stage primaryStage)
	{
		Stage countWindow = new Stage();
		countWindow.initStyle(StageStyle.TRANSPARENT);
		countWindow.initOwner(primaryStage);
		AnchorPane root = new AnchorPane();
		
	}
	public void createTransparentStage(Stage primaryStage, double x)
	{
		Stage second = new Stage();
		second.initStyle(StageStyle.TRANSPARENT);
		second.initOwner(primaryStage);
		
		AnchorPane root = new AnchorPane();
		Blueprint scene = new Blueprint(root, 300*wRatio, 200*hRatio);
		
		second.setScene(scene);
		second.setX(x);
		second.setY(90);
		second.show();
	}
}