package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;

import javax.swing.JOptionPane;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;
import static javafx.application.ConditionalFeature.CONTROLS;
import static javafx.application.ConditionalFeature.EFFECT;
import static javafx.application.ConditionalFeature.GRAPHICS;
import static javafx.application.ConditionalFeature.SCENE3D;

public class BoardTest extends Application {

	private static final double ANCHOR = 10.0;
	//test

	public static void main(String[] args) {
		launch(args);
	}

	private final PerspectiveCamera cameraView = new PerspectiveCamera();
	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	public final double dispWidth = displayRes.getWidth();
	public final double dispHeight = displayRes.getHeight();

	private final Timeline solid = new Timeline();

	public BoardTest() {

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
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
	private void init(Stage primaryStage) {
		ConditionalFeature[] featuresNeeded = { CONTROLS, EFFECT, GRAPHICS, SCENE3D };
		for (ConditionalFeature feature : featuresNeeded) {
			if (!Platform.isSupported(feature)) {
				JOptionPane.showMessageDialog(null, "Ooops! You don't have the needed graphics card for this application to render properly.");
				Platform.exit();
				System.exit(1);
			}
		}

		primaryStage.initStyle(StageStyle.TRANSPARENT);

		AnchorPane root = new AnchorPane();
		root.setDepthTest(DepthTest.ENABLE);

		Scene scene = new Scene(root, dispWidth, dispHeight, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.color(.85, .85, .85,.4));

		//image files needed.
		Image imgWood = new Image(ClassLoader.getSystemResourceAsStream("monopolycards/ui/test/Woodtexture.jpg"));
		Image imgRoughGreen = new Image(ClassLoader.getSystemResourceAsStream("monopolycards/ui/test/TableTop.png"));

		//Materials
		PhongMaterial woodTexture = new PhongMaterial();
		PhongMaterial greenTexture = new PhongMaterial();

		woodTexture.setSpecularColor(Color.WHITE);
		woodTexture.setBumpMap(imgWood);
		woodTexture.setDiffuseMap(imgWood);
		greenTexture.setBumpMap(imgRoughGreen);
		greenTexture.setDiffuseMap(imgRoughGreen);

		//parts
		Rotate boardDepth = new Rotate(0, Rotate.Y_AXIS);
		Group board = new Group();
		PointLight faceLight = new PointLight();
		Box buff = new Box(1000,1000, 10);
		Box table = new Box(dispWidth, dispHeight, 100);
		Box top = new Box(dispWidth - 200, dispHeight - 200, 1);

		//set parts properties.
		//buff.setSmooth(true);
		table.getTransforms().add(boardDepth);
		table.setMaterial(woodTexture);
		//table.setRotate(15);
		top.setMaterial(greenTexture);
		faceLight.setTranslateZ(10000);
		buff.setTranslateZ(-10);
		top.setTranslateZ(-10);

		//populate stuff.
		board.setTranslateX(dispWidth / 2);
		board.setTranslateY(dispHeight / 2);
		board.getChildren().addAll( top, table, faceLight, buff);
		root.getChildren().addAll(board);

		scene.setOnMouseEntered(e->trans(true));
		scene.setOnMouseExited(e->trans(false));


		Timeline rotator = new Timeline();
		rotator.getKeyFrames()
		.addAll(new KeyFrame(Duration.ZERO, new KeyValue(boardDepth.angleProperty(), 0)),
				new KeyFrame(Duration.seconds(4), new KeyValue(boardDepth.angleProperty(), -360)));

		rotator.setCycleCount(-1);
		rotator.play();
		primaryStage.setScene(scene);
		scene.setCamera(cameraView);
		primaryStage.show();
	}
}
