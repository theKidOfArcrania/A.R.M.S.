package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.util.ArrayList;

import armsgame.res.Tools;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class MoveCounter extends Application {

	private static int count;

	public static void main(String[] args) {
		launch(args);
	}

	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice()
		.getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	private final double dispWidth = displayRes.getWidth();
	private final double dispHeight = displayRes.getHeight();
	private final double wRatio = dispWidth / 1600;
	private final double hRatio = dispHeight / 900;

	public double sRatio;
	private final SimpleDoubleProperty alph;
	private final Timeline solid = new Timeline();
	public ArrayList<ImageView> lightList;

	public MoveCounter() {
		alph = new SimpleDoubleProperty(1.0);
		sRatio = checkSmallRatio();
		lightList = new ArrayList<ImageView>();
		count = 3;

	}

	public double checkSmallRatio() {
		return (wRatio > hRatio) ? hRatio : wRatio;
	}

	public void genAnimation(ImageView light) {

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

	public void lightOff(ArrayList<ImageView> lightList) {
		count--;
		if (count < 0) {
			resetMoves(lightList);
		} else {
			genAnimation(lightList.get(count));
		}
	}

	public void resetMoves(ArrayList<ImageView> lightList) {
		count = lightList.size();
		for (ImageView light : lightList) {
			light.setOpacity(1.0);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}

	private void init(Stage primaryStage) {
		primaryStage.initStyle(StageStyle.TRANSPARENT);

		AnchorPane root = new AnchorPane();
		Scene scene = new Scene(root, 290 * wRatio, 140 * hRatio);
		InnerShadow smallShade = new InnerShadow(2.0, Color.BLACK);
		InnerShadow mediumShade = new InnerShadow(3.0, Color.BLACK);
		InnerShadow largeShade = new InnerShadow(5.0, Color.BLACK);
		DropShadow out = new DropShadow(2.0, Color.BLACK);

		Rectangle panel = Tools.createRoundedRectangle(217, 140, 60, 60, 22, 52, sRatio, wRatio, hRatio, Color.DARKGRAY.darker()
			.darker(), largeShade);
		Text name = Tools.createText(0, 15, wRatio, hRatio, "Number of Moves", Color.GRAY, smallShade, Tools.createBoldFont(24, sRatio));
		name.setWrappingWidth(dispWidth / 6);
		name.setTextAlignment(TextAlignment.CENTER);

		Image lightoff = Tools.createImage("lightoff.png");
		Image light = Tools.createImage("Greenlight.png");
		Image screw = Tools.createImage("screw.png");

		Image background = Tools.createImage("background.jpg");
		ImageView light1 = Tools.createImageView(light, 56, 56, 36, 91.5, sRatio, wRatio, hRatio, mediumShade);
		lightList.add(light1);
		ImageView light2 = Tools.createImageView(light, 56, 56, 102.5, 91.5, sRatio, wRatio, hRatio, mediumShade);
		lightList.add(light2);
		ImageView light3 = Tools.createImageView(light, 56, 56, 168, 91.5, sRatio, wRatio, hRatio, mediumShade);
		lightList.add(light3);
		ImageView light1off = Tools.createImageView(lightoff, 56, 56, 36, 91.5, sRatio, wRatio, hRatio, largeShade);
		ImageView light2off = Tools.createImageView(lightoff, 56, 56, 102.5, 91.5, sRatio, wRatio, hRatio, largeShade);
		ImageView light3off = Tools.createImageView(lightoff, 56, 56, 168, 91.5, sRatio, wRatio, hRatio, largeShade);
		ImageView screw1 = Tools.createImageView(screw, 15, 15, 0.0, 0.0, sRatio, wRatio, hRatio, out);
		ImageView screw2 = Tools.createImageView(screw, 15, 15, 250, 0.0, sRatio, wRatio, hRatio, out);
		ImageView screw3 = Tools.createImageView(screw, 15, 15, 0.0, 205, sRatio, wRatio, hRatio, out);
		ImageView screw4 = Tools.createImageView(screw, 15, 15, 250, 205, sRatio, wRatio, hRatio, out);

		BackgroundImage back = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		root.getChildren()
			.addAll(name, panel, light1off, light2off, light3off, light1, light2, light3, screw1, screw2, screw3, screw4);
		root.setOnMouseClicked(e -> lightOff(lightList));
		root.setBackground(new Background(back));
		root.setEffect(largeShade);
		primaryStage.setX(0);
		primaryStage.setY(dispHeight - scene.getHeight());

		primaryStage.setScene(scene);

		primaryStage.show();

	}
}